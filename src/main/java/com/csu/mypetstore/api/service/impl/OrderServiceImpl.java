package com.csu.mypetstore.api.service.impl;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.Order;
import com.csu.mypetstore.api.domain.OrderItem;
import com.csu.mypetstore.api.domain.Product;
import com.csu.mypetstore.api.domain.structMapper.OrderStructMapper;
import com.csu.mypetstore.api.domain.structMapper.ProductStructMapper;
import com.csu.mypetstore.api.domain.vo.OrderItemVO;
import com.csu.mypetstore.api.domain.vo.OrderVO;
import com.csu.mypetstore.api.exception.InsertException;
import com.csu.mypetstore.api.persistence.OrderItemMapper;
import com.csu.mypetstore.api.persistence.OrderMapper;
import com.csu.mypetstore.api.persistence.ProductMapper;
import com.csu.mypetstore.api.service.OrderService;
import com.csu.mypetstore.api.util.BigDecimalUtils;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements OrderService {
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderServiceImpl(ProductMapper productMapper, OrderMapper orderMapper, OrderItemMapper orderItemMapper) {
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public CommonResponse<OrderVO> createOrder(Integer userId, Integer addressId, Integer productId, Integer quantity) {
        // 生成订单并存入数据库
        Product product = productMapper.selectById(productId);
        if (product == null || product.getStatus() != CONSTANT.ProductStatus.ON_SALE.getCode())
            return CommonResponse.createResponseForError("产品不存在或已下架");
        if (product.getStock() < quantity)
            return CommonResponse.createResponseForError("库存不足");

        OrderItem orderItem = ProductStructMapper.INSTANCE.product2OrderItem(product);
        orderItem.setUid(userId);
        orderItem.setQuantity(quantity);
        orderItem.setTotalPrice(BigDecimalUtils.multiply(product.getPrice().doubleValue(), quantity));
        List<OrderItem> orderItemList = Lists.newArrayList(orderItem);
        Order order = createOrderBean(orderItemList, userId, addressId);

////         用于并发测试锁
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // 更新商品库存
        insertOrderAndItem(order, orderItemList);
        product.setStock(product.getStock() - quantity);
        long row = productMapper.updateById(product);  // 设置了乐观锁，更新失败时需要回滚订单信息
        if (row != 1) {
            rollback(order, orderItemList);
            return CommonResponse.createResponseForError("库存已更新，请重新提交订单");
        }

        // 返回订单信息
        OrderVO orderVO = order2VO(order, orderItemList);
        return CommonResponse.createResponseForSuccess(orderVO);
    }

    @Override
    public CommonResponse<OrderVO> createOrder(Integer userId, Integer addressId) {
        return null;
    }

    private long generateOrderNo(){
        return System.currentTimeMillis() + new Random().nextInt(1000);
    }

    private Order createOrderBean(List<OrderItem> orderItemList, Integer userId, Integer addressId) {
        LocalDateTime now = LocalDateTime.now();
        long orderNo = generateOrderNo();
        AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(new BigDecimal("0"));

        orderItemList.forEach(orderItem -> {
            orderItem.setOrderNo(orderNo);
            orderItem.setCreateTime(now);
            orderItem.setUpdateTime(now);
            totalPrice.set(BigDecimalUtils.add(totalPrice.get().doubleValue(), orderItem.getTotalPrice().doubleValue()));
        });

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUid(userId);
        order.setAddressId(addressId);
        order.setPaymentPrice(totalPrice.get());
        // 支付类型支付时填写
        order.setPostage(getPostage(addressId));
        order.setStatus(CONSTANT.OrderStatus.UNPAID.getCode());
        //支付时间、发货时间、交易完成时间和关单时间未来再填入
        order.setCreateTime(now);
        order.setUpdateTime(now);
        return order;
    }

    private Integer getPostage(Integer addressId) {
        // TODO: 获取邮费
        return 10;
    }

    private void insertOrderAndItem(Order order, List<OrderItem> orderItemList) {
        int result = orderMapper.insert(order);
        if(result != 1){
            throw new InsertException("订单");
        }

        List<OrderItem> rollbackList = Lists.newArrayList();
        for (OrderItem orderItem: orderItemList) {
            int row = orderItemMapper.insert(orderItem);
            if (row != 1) {  // 失败，回滚报错
                rollback(order, rollbackList);
                throw new InsertException("订单");
            }
            rollbackList.add(orderItem);
        }
    }

    private void rollback(Order order, List<OrderItem> rollbackList) {
        orderMapper.deleteById(order);
        rollbackList.forEach(orderItemMapper::deleteById);
    }

    private OrderVO order2VO(Order order, List<OrderItem> orderItemList) {
        List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        orderItemList.forEach(orderItem ->
                orderItemVOList.add(OrderStructMapper.INSTANCE.orderItem2VO(orderItem)));

        OrderVO orderVO = OrderStructMapper.INSTANCE.order2VO(order);
        orderVO.setOrderItemVOList(orderItemVOList);

        return orderVO;
    }

}
