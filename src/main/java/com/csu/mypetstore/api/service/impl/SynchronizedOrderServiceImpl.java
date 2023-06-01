package com.csu.mypetstore.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import com.csu.mypetstore.api.domain.*;
import com.csu.mypetstore.api.domain.structMapper.AddressStructMapper;
import com.csu.mypetstore.api.domain.structMapper.OrderStructMapper;
import com.csu.mypetstore.api.domain.vo.AddressVO;
import com.csu.mypetstore.api.domain.vo.OrderItemVO;
import com.csu.mypetstore.api.domain.vo.OrderListVO;
import com.csu.mypetstore.api.domain.vo.OrderVO;
import com.csu.mypetstore.api.exception.APIException;
import com.csu.mypetstore.api.exception.InsertException;
import com.csu.mypetstore.api.persistence.*;
import com.csu.mypetstore.api.service.OrderService;
import com.csu.mypetstore.api.util.BigDecimalUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
public class SynchronizedOrderServiceImpl implements OrderService {
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartItemMapper cartItemMapper;
    private final AddressMapper addressMapper;

    public SynchronizedOrderServiceImpl(ProductMapper productMapper, OrderMapper orderMapper, OrderItemMapper orderItemMapper, CartItemMapper cartItemMapper, AddressMapper addressMapper) {
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.cartItemMapper = cartItemMapper;
        this.addressMapper = addressMapper;
    }
    /*根据指定商品生成订单*/
    @Override
    @Transactional  // 开始事务，出现错误自动回滚，实际逻辑是正确返回了才会提交事务
    public CommonResponse<OrderVO> createOrder(Integer userId, Integer addressId, Integer productId, Integer quantity) {
        Map<Integer, Integer> productIdAndQuantityMap = Maps.newHashMap();
        productIdAndQuantityMap.put(productId, quantity);

        Map<Product, Integer> productAndQuantityMap = updateProductQuantity(productIdAndQuantityMap);

        OrderVO orderVo = createOrder(userId, addressId, productAndQuantityMap);
        return CommonResponse.createResponseForSuccess(orderVo);
    }

    /*根据用户购物车生成订单*/
    @Override
    @Transactional
    public CommonResponse<OrderVO> createOrder(Integer userId, Integer addressId) {
        Map<Integer, Integer> productIdAndQuantityMap = Maps.newHashMap();
        List<CartItem> cartItemList = cartItemMapper.selectList(Wrappers.<CartItem>query().eq("uid", userId).eq("selected", true));
        if (CollectionUtils.isEmpty(cartItemList))
            return CommonResponse.createResponseForError("购物车为空或没有选中的商品");
        cartItemList.forEach(cartItem -> productIdAndQuantityMap.put(cartItem.getProductId(), cartItem.getQuantity()));

        Map<Product, Integer> productAndQuantityMap = updateProductQuantity(productIdAndQuantityMap);

        OrderVO orderVO = createOrder(userId, addressId, productAndQuantityMap);
        return CommonResponse.createResponseForSuccess(orderVO);
    }

    // 加锁方法，读取产品数量并更新
    private synchronized Map<Product, Integer> updateProductQuantity(Map<Integer, Integer> productIdAndQuantityMap) {
        Map<Product, Integer> productAndQuantityMap = Maps.newHashMap();
        productIdAndQuantityMap.forEach((productId, quantity) -> {
            Product product = productMapper.selectById(productId);
            if (product == null) throw new APIException(String.format("商品(id:%d)不存在", productId));
            if (product.getStatus() != CONSTANT.ProductStatus.ON_SALE.getCode()) throw new APIException(String.format("商品(%s)已下架", product.getName()));
            if (product.getStock() < quantity) throw new APIException(String.format("商品(%S)库存不足", product.getName()));

            product.setStock(product.getStock() - quantity);
            long row = productMapper.updateById(product);
            if (row != 1) throw new InsertException("商品");
            productAndQuantityMap.put(product, quantity);
        });

        return productAndQuantityMap;
    }

    // 生成订单、存储、返回订单
    private OrderVO createOrder(Integer userId, Integer addressId, Map<Product, Integer> productAndQuantityMap) {
        LocalDateTime now = LocalDateTime.now();
        long orderNo = generateOrderNo();
        AtomicReference<BigDecimal> orderTotalPrice = new AtomicReference<>(new BigDecimal("0"));

        // 1.生成orderItem
        List<OrderItem> orderItemList = Lists.newArrayList();
        productAndQuantityMap.forEach((product, quantity) -> {
            OrderItem orderItem = OrderStructMapper.INSTANCE.product2OrderItem(product);
            orderItem.setOrderNo(orderNo);
            orderItem.setUid(userId);
            orderItem.setQuantity(quantity);
            orderItem.setTotalPrice(BigDecimalUtils.multiply(product.getPrice().doubleValue(), quantity));
            orderItem.setCreateTime(now);
            orderItem.setUpdateTime(now);
            orderTotalPrice.set(BigDecimalUtils.add(orderTotalPrice.get().doubleValue(), orderItem.getTotalPrice().doubleValue()));
            orderItemList.add(orderItem);
        });

        // 2.生成order
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUid(userId);
        order.setAddressId(addressId);
        // 支付类型支付时填写
        order.setPaymentPrice(orderTotalPrice.get());
        order.setPostage(getPostage(addressId));
        order.setStatus(CONSTANT.OrderStatus.UNPAID.getCode());
        //支付时间、发货时间、交易完成时间和关单时间未来再填入
        order.setCreateTime(now);
        order.setUpdateTime(now);

        // 3.将order和orderItem存入数据库
        long row = orderMapper.insert(order);
        if (row != 1)
            throw new InsertException("订单");
        for (OrderItem orderItem: orderItemList) {
            row = orderItemMapper.insert(orderItem);
            if (row != 1)
                throw new InsertException("订单");
        }

        // 4.转化为VO
        return order2VO(order, orderItemList);
    }

    private OrderVO order2VO(Order order, List<OrderItem> orderItemList) {
        List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        orderItemList.forEach(orderItem ->
                orderItemVOList.add(OrderStructMapper.INSTANCE.orderItem2VO(orderItem)));

        OrderVO orderVO = OrderStructMapper.INSTANCE.order2VO(order);
        orderVO.setOrderItemVOList(orderItemVOList);
        Address address = addressMapper.selectById(order.getAddressId());
        if (address == null)
            throw new APIException("地址不存在");
        orderVO.setAddressVO(AddressStructMapper.INSTANCE.address2VO(address));
        return orderVO;
    }

    private Integer getPostage(Integer addressId) {
        // TODO: 获取邮费
        return 10;
    }

    private long generateOrderNo(){
        return System.currentTimeMillis() + new Random().nextInt(1000);
    }


    @Override
    public CommonResponse<OrderVO> deleteOrder(Integer userId, Long orderNo) {
        Order order = orderMapper.selectOne(Wrappers.<Order>query().eq("uid", userId).eq("order_no", orderNo));
        List<OrderItem> orderItemList = orderItemMapper.selectList(Wrappers.<OrderItem>query().eq("uid", userId).eq("order_no", orderNo));
        if (order == null || CollectionUtils.isEmpty(orderItemList)) return CommonResponse.createResponseForError("未查询到对应订单");

        if (order.getStatus() >= CONSTANT.OrderStatus.PAID.getCode())  // 只有未付款的订单能够被取消
            return CommonResponse.createResponseForError(ResponseCode.PAID_ORDER.getDescription(), ResponseCode.PAID_ORDER.getCode());

        order.setStatus(CONSTANT.OrderStatus.CANCEL.getCode());
        long row = orderMapper.updateById(order);
        if (row != 1) return CommonResponse.createResponseForError("修改订单失败");

        OrderVO orderVO = order2VO(order, orderItemList);
        return CommonResponse.createResponseForSuccess(orderVO);
    }

    @Override
    public CommonResponse<OrderVO> getOrderById(Integer userId, Long orderNo) {
        Order order = orderMapper.selectOne(Wrappers.<Order>query().eq("uid", userId).eq("order_no", orderNo));
        List<OrderItem> orderItemList = orderItemMapper.selectList(Wrappers.<OrderItem>query().eq("uid", userId).eq("order_no", orderNo));
        if (order == null || CollectionUtils.isEmpty(orderItemList)) return CommonResponse.createResponseForError("未查询到对应订单");

        OrderVO orderVO = order2VO(order, orderItemList);
        return CommonResponse.createResponseForSuccess(orderVO);

    }

    @Override
    public CommonResponse<List<OrderListVO>> getOrderList(Integer userId) {
        List<Order> orderList = orderMapper.selectList(Wrappers.<Order>query().eq("uid", userId));
        if (CollectionUtils.isEmpty(orderList)) return CommonResponse.createResponseForError("未查询到对应订单");

        List<OrderListVO> orderListVOList = Lists.newArrayListWithCapacity(orderList.size());
        for (Order order: orderList) {
            List<OrderItem> orderItemList = orderItemMapper.selectList(Wrappers.<OrderItem>query().eq("order_no", order.getOrderNo()));
            List<String> orderItemNameList = Lists.newArrayListWithCapacity(orderItemList.size());
            orderItemList.forEach(orderItem -> orderItemNameList.add(orderItem.getProductName()));

            AddressVO addressVO = AddressStructMapper.INSTANCE.address2VO(addressMapper.selectById(order.getAddressId()));
            OrderListVO orderListVO = OrderStructMapper.INSTANCE.order2ListVO(order);

            orderListVO = orderListVO
                    .withProductNameList(orderItemNameList)
                    .withAddressVO(addressVO);
            orderListVOList.add(orderListVO);
        }

        return CommonResponse.createResponseForSuccess(orderListVOList);
    }
}
