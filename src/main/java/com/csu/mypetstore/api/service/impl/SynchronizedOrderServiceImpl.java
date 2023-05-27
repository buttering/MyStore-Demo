package com.csu.mypetstore.api.service.impl;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.Order;
import com.csu.mypetstore.api.domain.OrderItem;
import com.csu.mypetstore.api.domain.Product;
import com.csu.mypetstore.api.domain.vo.OrderVO;
import com.csu.mypetstore.api.exception.APIException;
import com.csu.mypetstore.api.exception.InsertException;
import com.csu.mypetstore.api.persistence.CartItemMapper;
import com.csu.mypetstore.api.persistence.OrderItemMapper;
import com.csu.mypetstore.api.persistence.OrderMapper;
import com.csu.mypetstore.api.persistence.ProductMapper;
import com.csu.mypetstore.api.service.OrderService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SynchronizedOrderServiceImpl implements OrderService {
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartItemMapper cartItemMapper;

    public SynchronizedOrderServiceImpl(ProductMapper productMapper, OrderMapper orderMapper, OrderItemMapper orderItemMapper, CartItemMapper cartItemMapper) {
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.cartItemMapper = cartItemMapper;
    }
    /*根据指定商品生成订单*/
    @Override
    @Transactional  // 开始事务，出现错误自动回滚
    public CommonResponse<OrderVO> createOrder(Integer userId, Integer addressId, Integer productId, Integer quantity) {

        return null;
    }

    /*根据用户购物车生成订单*/
    @Override
    @Transactional
    public CommonResponse<OrderVO> createOrder(Integer userId, Integer addressId) {
        return null;
    }

    // 加锁方法，读取产品数量并更新
    private synchronized Map<Product, Integer> updateProductQuantity(Map<Integer, Integer> productIdAndQuantityMap) {

    }

    // 生成订单、存储、返回订单
    private OrderVO createOrder(Integer userId, Integer addressId, Map<Product, Integer>) {

    }

    private Integer getPostage(Integer addressId) {
        // TODO: 获取邮费
        return 10;
    }
}
