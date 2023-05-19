package com.csu.mypetstore.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import com.csu.mypetstore.api.util.ListBeanUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
//        Product product = productMapper.selectById(productId);
//        if (product == null || product.status() != CONSTANT.ProductStatus.ON_SALE.getCode())
//            return CommonResponse.createResponseForError("商品不存在或已下架");
//
//        CartItem cartItem = cartItemMapper.selectOne(Wrappers.<CartItem>query().eq("uid", userId).eq("pid", productId));
//
//        int row;
//        if (cartItem == null) {
//            // 不包含，新增项
//        } else {
//            // 包含，修改项
//        }
//
//        CartVO cartVO = getCartVOAndCheckStock();
        return null;
    }

    private CartVO getCartVOAndCheckStock(Integer userID) {
//        List<CartItem> cartItemList = cartItemMapper.selectList(Wrappers.<CartItem>query().eq("uid", userID));
//        List<CartItemVO> cartItemVOList = Lists.newArrayList();
//
//        BigDecimal cartTotalPrice = new BigDecimal("0");
//        boolean allSelected = true;
//
//        if (CollectionUtils.isNotEmpty(cartItemList)) {
//            cartItemVOList = ListBeanUtils.copyListProperties(
//                    cartItemList,
//                    CartItemVO::new,
//                    (cartItem, cartItemVO) -> {
//                        Product product = productMapper.selectById(cartItem.productId());
//                        if (product != null) {
//
//
//                            cartItemVO = ProductStructMapper.INSTANCE.product2CartItemVO(cartItemVO, product);
//                            cartItemVO.setImageList(productService.getImageToken(product.id(), false));
//                            cartItemVO.setProductTotalPrice();
//                            // TODO: 处理库存问题
//                            // TODO: 为productName等属性进行赋值
//                        }
//                    }
//            );
//        }
        return null;
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
