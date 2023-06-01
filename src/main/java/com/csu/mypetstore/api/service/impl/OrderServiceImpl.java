package com.csu.mypetstore.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.CartItem;
import com.csu.mypetstore.api.domain.Order;
import com.csu.mypetstore.api.domain.OrderItem;
import com.csu.mypetstore.api.domain.Product;
import com.csu.mypetstore.api.domain.structMapper.OrderStructMapper;
import com.csu.mypetstore.api.domain.vo.OrderItemVO;
import com.csu.mypetstore.api.domain.vo.OrderListVO;
import com.csu.mypetstore.api.domain.vo.OrderVO;
import com.csu.mypetstore.api.exception.InsertException;
import com.csu.mypetstore.api.persistence.CartItemMapper;
import com.csu.mypetstore.api.persistence.OrderItemMapper;
import com.csu.mypetstore.api.persistence.OrderMapper;
import com.csu.mypetstore.api.persistence.ProductMapper;
import com.csu.mypetstore.api.service.OrderService;
import com.csu.mypetstore.api.util.BigDecimalUtils;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements OrderService {
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartItemMapper cartItemMapper;

    public OrderServiceImpl(ProductMapper productMapper, OrderMapper orderMapper, OrderItemMapper orderItemMapper, CartItemMapper cartItemMapper) {
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.cartItemMapper = cartItemMapper;
    }

//    /*根据指定商品生成订单*/
//    @Override
//    @Transactional  // 开始事务，出现错误自动回滚
//    public CommonResponse<OrderVO> createOrder(Integer userId, Integer addressId, Integer productId, Integer quantity) {
//        // 生成订单并存入数据库
//        Product product = productMapper.selectById(productId);
//        if (product == null || product.getStatus() != CONSTANT.ProductStatus.ON_SALE.getCode())
//            return CommonResponse.createResponseForError("产品不存在或已下架");
//        if (product.getStock() < quantity)
//            return CommonResponse.createResponseForError("库存不足");
//
//        OrderItem orderItem = OrderStructMapper.INSTANCE.product2OrderItem(product);
//        orderItem.setUid(userId);
//        orderItem.setQuantity(quantity);
//        orderItem.setTotalPrice(BigDecimalUtils.multiply(product.getPrice().doubleValue(), quantity));
//        List<OrderItem> orderItemList = Lists.newArrayList(orderItem);
//        Order order = createOrderBean(orderItemList, userId, addressId);
//
//        insertOrderAndItem(order, orderItemList);
//
//        // 更新商品库存
//        product.setStock(product.getStock() - quantity);
//        long row = productMapper.updateById(product);
//        if (row != 1) {
//            rollback(order, orderItemList);
//            return CommonResponse.createResponseForError("库存已更新，请重新提交订单");
//        }
//
//
//
//
//    }

    @Override
    // 根据指定商品生成订单
    public CommonResponse<OrderVO> createOrder(Integer userId, Integer addressId, Integer productId, Integer quantity) {
        // 生成订单并存入数据库
        Product product = productMapper.selectById(productId);
        if (product == null || product.getStatus() != CONSTANT.ProductStatus.ON_SALE.getCode())
            return CommonResponse.createResponseForError("产品不存在或已下架");
        if (product.getStock() < quantity)
            return CommonResponse.createResponseForError("库存不足");

        OrderItem orderItem = OrderStructMapper.INSTANCE.product2OrderItem(product);
        orderItem.setUid(userId);
        orderItem.setQuantity(quantity);
        orderItem.setTotalPrice(BigDecimalUtils.multiply(product.getPrice().doubleValue(), quantity));
        List<OrderItem> orderItemList = Lists.newArrayList(orderItem);
        Order order = createOrderBean(orderItemList, userId, addressId);
        insertOrderAndItem(order, orderItemList);

////      用于并发测试锁
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // 更新商品库存
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
    // 从购物车中生成订单
    public CommonResponse<OrderVO> createOrder(Integer userId, Integer addressId) {
        List<CartItem> cartItemList = cartItemMapper.selectList(Wrappers.<CartItem>query().eq("uid", userId));
        if (cartItemList == null)
            return CommonResponse.createResponseForError("购物车是空的");

        // 将购物车转化为订单并存入数据库
        List<OrderItem> orderItemList = Lists.newArrayList();
        List<Product> insufficientProductList = Lists.newArrayList();   // 记录库存不足和被锁的商品
        Map<Product, Integer> sufficientProductMap = new HashMap<>();  // 记录需要被修改库存的商品和库存，重写了Product的hashCode方法
        for (CartItem cartItem: cartItemList) {
            // 依次检查商品库存是否满足
            Product product = productMapper.selectById(cartItem.getProductId());
            if (product.getStock() < cartItem.getQuantity())
                insufficientProductList.add(product);
            else {
                OrderItem orderItem = OrderStructMapper.INSTANCE.cartItemAndProduct2OrderItem(cartItem, product);
                orderItem.setTotalPrice(BigDecimalUtils.multiply(orderItem.getCurrentPrice().doubleValue(), orderItem.getQuantity()));
                orderItemList.add(orderItem);
                sufficientProductMap.put(product, orderItem.getQuantity());
            }
        }
        Order order = createOrderBean(orderItemList, userId, addressId);
        insertOrderAndItem(order, orderItemList);

        // 更新商品库存
        for (Map.Entry<Product, Integer> productIntegerEntry: sufficientProductMap.entrySet())  {
            Product product = productIntegerEntry.getKey();
            Integer stock = productIntegerEntry.getValue();
            product.setStock(product.getStock() - stock);
            long row = productMapper.updateById(product);
            if (row != 1){  // 更新失败时不回滚，只是简单地不在最终订单中包含，并告知客户端
                insufficientProductList.add(product);
            }
        }
        return null;
    }

    private long generateOrderNo(){
        return System.currentTimeMillis() + new Random().nextInt(1000);
    }

    // 将order和orderItem的orderNO、createTime和updateTime设置为一致。
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

//    private void insertOrderAndItem(Order order, List<OrderItem> orderItemList) {
//        int result = orderMapper.insert(order);
//        if(result != 1){
//            throw new InsertException("订单");  // spring事务默认回滚RuntimeException，InsertException继承了RuntimeException
//        }
//        for (OrderItem orderItem: orderItemList) {
//            int row = orderItemMapper.insert(orderItem);
//            if (row != 1) {
//                throw new InsertException("订单");
//            }
//        }
//    }

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

    @Override
    public CommonResponse<OrderVO> deleteOrder(Integer userId, Long order_no) {
        return null;
    }

    @Override
    public CommonResponse<OrderVO> getOrderById(Integer userId, Long orderNo) {
        return null;
    }

    @Override
    public CommonResponse<List<OrderListVO>> getOrderList(Integer userId) {
        return null;
    }
}
