package com.csu.mypetstore.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.Category;
import com.csu.mypetstore.api.domain.Product;
import com.csu.mypetstore.api.domain.ProductImage;
import com.csu.mypetstore.api.domain.structMapper.ProductStructMapper;
import com.csu.mypetstore.api.domain.vo.ProductDetailVO;
import com.csu.mypetstore.api.domain.vo.TencentCOSVO;
import com.csu.mypetstore.api.persistence.CategoryMapper;
import com.csu.mypetstore.api.persistence.ProductImageMapper;
import com.csu.mypetstore.api.persistence.ProductMapper;
import com.csu.mypetstore.api.service.COSService;
import com.csu.mypetstore.api.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final COSService cosService;
    private final ProductImageMapper productImageMapper;

    ProductServiceImpl(ProductMapper productMapper, CategoryMapper categoryMapper, COSService cosService, ProductImageMapper productImageMapper) {
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
        this.cosService = cosService;
        this.productImageMapper = productImageMapper;
    }

    @Override
    public CommonResponse<ProductDetailVO> getProductDetail(Integer pid) {
        Product product = productMapper.selectById(pid);

        if (product == null || product.status() != CONSTANT.ProductStatus.ON_SALE.getCode())
            return CommonResponse.createResponseForError("商品不存在或已下架");

        Category category = categoryMapper.selectById(product.categoryId());
        List<ProductImage> productImageList = productImageMapper.selectList(Wrappers.<ProductImage>query().eq("pid", pid));
        ProductDetailVO productDetailVO = ProductStructMapper.INSTANCE.product2DetailVO(product, getImageToken(productImageList), category.parentId());

        return CommonResponse.createResponseForSuccess(productDetailVO);
    }

    private List<Map<String, Object>> getImageToken(List<ProductImage> productImageList) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (ProductImage productImage: productImageList) {
            String imageId = productImage.getId();
            TencentCOSVO token = (TencentCOSVO) cosService.generatePolicy(imageId).getData();

            Map<String, Object> map = new HashMap<>();
            map.put("image", productImage);
            map.put("token", token);
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public CommonResponse<List<Product>> getProductList(Integer cid, String keyword, String orderBy, int pageNum, int pageSize) {
        // TODO: 对象拼接合成
        return null;
    }
}
