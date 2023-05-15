package com.csu.mypetstore.api.service.impl;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.Category;
import com.csu.mypetstore.api.domain.Product;
import com.csu.mypetstore.api.persistence.CategoryMapper;
import com.csu.mypetstore.api.persistence.ProductMapper;
import com.csu.mypetstore.api.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;

    ProductServiceImpl(ProductMapper productMapper, CategoryMapper categoryMapper) {
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CommonResponse<Product> getProductDetail(Integer pid) {
        Product product = productMapper.selectById(pid);

        if (product == null || product.status() != CONSTANT.ProductStatus.ON_SALE.getCode())
            return CommonResponse.createResponseForError("商品不存在或已下架");

        Category category = categoryMapper.selectById(product.categoryId());
        Integer parentCategoryId = category.parentId();
        // TODO: 合成对象
        return CommonResponse.createResponseForSuccess(product);
    }

    @Override
    public CommonResponse<List<Product>> getProductList(Integer cid, String keyword, String orderBy, int pageNum, int pageSize) {
        // TODO: 对象拼接合成
        return null;
    }
}
