package com.csu.mypetstore.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.CartItem;
import com.csu.mypetstore.api.domain.Product;
import com.csu.mypetstore.api.domain.structMapper.ProductStructMapper;
import com.csu.mypetstore.api.domain.vo.CartItemVO;
import com.csu.mypetstore.api.domain.vo.CartVO;
import com.csu.mypetstore.api.persistence.CartItemMapper;
import com.csu.mypetstore.api.persistence.ProductMapper;
import com.csu.mypetstore.api.service.CartService;
import com.csu.mypetstore.api.service.ProductService;
import com.csu.mypetstore.api.util.BigDecimalUtils;
import com.csu.mypetstore.api.util.ListBeanUtils;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CartServiceImpl implements CartService {
    private final CartItemMapper cartItemMapper;
    private final ProductMapper productMapper;
    private final ProductService productService;

    public CartServiceImpl(CartItemMapper cartItemMapper, ProductMapper productMapper, ProductService productService) {
        this.cartItemMapper = cartItemMapper;
        this.productMapper = productMapper;
        this.productService = productService;
    }

    @Override
    public CommonResponse<CartVO> addCartItem(Integer userId, Integer productId, Integer quantity) {
        Product product = productMapper.selectById(productId);
        if (product == null || product.status() != CONSTANT.ProductStatus.ON_SALE.getCode())
            return CommonResponse.createResponseForError("商品不存在或已下架");

        CartItem cartItem = cartItemMapper.selectOne(Wrappers.<CartItem>query().eq("uid", userId).eq("product_id", productId));

        if (cartItem == null) {
            // 不包含，新增项
            cartItem = new CartItem(null, userId, productId, quantity, true, LocalDateTime.now(), LocalDateTime.now());
            cartItemMapper.insert(cartItem);  // 错误放到全局异常处理
        } else {
            // 包含，修改项
            UpdateWrapper<CartItem> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", cartItem.getId())
                    .set("quantity", quantity + cartItem.getQuantity())
                    .set("update_time", LocalDateTime.now());
            cartItemMapper.update(null, updateWrapper);
        }

        return CommonResponse.createResponseForSuccess(getCartVOAndCheckStock(userId));
    }

    private CartVO getCartVOAndCheckStock(Integer userId) {
        List<CartItem> cartItemList = cartItemMapper.selectList(Wrappers.<CartItem>query().eq("uid", userId));
        List<CartItemVO> cartItemVOList = Lists.newArrayList();

        // 在lambda表达式中对变量的操作都是基于原变量的副本，不会影响到原变量的值。要求局部变量必须是 final 类型的
        // java8之后，如果不是 final 类型的话，编译器自动加上 final 修饰符，即effectively final
        // Atomic家族主要是保证多线程环境下的原子性，相比synchronized而言更加轻量级。比较常用的是AtomicInteger，作用是对Integer类型操作的封装，而AtomicReference作用是对普通对象的封装。
        AtomicReference<BigDecimal> cartTotalPrice = new AtomicReference<>(new BigDecimal("0"));
        AtomicBoolean allSelected = new AtomicBoolean(true);

        if (CollectionUtils.isNotEmpty(cartItemList)) {
            // 因为structMapper并不是对对象的属性直接修改，而是构造了一个新的对象。因此需要使用BiFunction类型的函数式接口
            cartItemVOList = ListBeanUtils.copyListProperties(
                    cartItemList,
                    CartItemVO::new,
                    (cartItem, cartItemVO) -> {
                        Product product = productMapper.selectById(cartItem.getProductId());
                        if (product != null) {
                            cartItemVO = ProductStructMapper.INSTANCE.product2CartItemVO(cartItemVO, product);

                            // 判断库存
                            if (product.stock() >= cartItem.getQuantity()) {
                                cartItemVO.setQuantity(cartItemVO.getQuantity());
                                cartItemVO.setCheckStock(CONSTANT.CART_ITEM_CHECK_STOCK.STOCK_SUFFICIENT);
                            } else {  // 库存不足时，修改购物车及数据库并设置标志
                                UpdateWrapper<CartItem> updateWrapper = new UpdateWrapper<>();
                                updateWrapper.eq("id", cartItem.getId())
                                        .set("quantity", product.stock());
                                cartItemMapper.update(null, updateWrapper);
                                cartItemVO.setQuantity(product.stock());
                                cartItemVO.setCheckStock(CONSTANT.CART_ITEM_CHECK_STOCK.STOCK_INSUFFICIENT);
                            }

                            cartItemVO.setImageList(productService.getImageToken(product.id(), true));
                            cartItemVO.setProductTotalPrice(BigDecimalUtils.multiply(cartItemVO.getQuantity(), cartItemVO.getProductPrice().doubleValue()));

                            // 计算总价，仅统计勾选的
                            if (cartItem.getSelected())
                                cartTotalPrice.set(BigDecimalUtils.add(cartTotalPrice.get().doubleValue(), cartItemVO.getProductTotalPrice().doubleValue()));
                            else
                                allSelected.set(false);
                        }
                        return cartItemVO;
                    }
            );
        }
        return new CartVO(cartItemVOList, cartTotalPrice.get(), allSelected.get());

//        return null;
    }

    @Override
    public CommonResponse<CartVO> updateCart(Integer userId, Integer productId, Integer quantity) {
        return null;
    }

    @Override
    public CommonResponse<CartVO> getCart(Integer userId) {
        return null;
    }

    @Override
    public CommonResponse<CartVO> deleteCart(Integer userId, Integer productId, Integer quantity) {
        return null;
    }


}
