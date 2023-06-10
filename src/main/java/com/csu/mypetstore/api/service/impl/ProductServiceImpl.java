package com.csu.mypetstore.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import com.csu.mypetstore.api.domain.Category;
import com.csu.mypetstore.api.domain.Product;
import com.csu.mypetstore.api.domain.ProductImage;
import com.csu.mypetstore.api.domain.structMapper.ProductStructMapper;
import com.csu.mypetstore.api.domain.vo.ProductDetailVO;
import com.csu.mypetstore.api.domain.vo.ProductListVO;
import com.csu.mypetstore.api.domain.vo.TencentCOSVO;
import com.csu.mypetstore.api.persistence.CategoryMapper;
import com.csu.mypetstore.api.persistence.ProductImageMapper;
import com.csu.mypetstore.api.persistence.ProductMapper;
import com.csu.mypetstore.api.service.COSService;
import com.csu.mypetstore.api.service.CategoryService;
import com.csu.mypetstore.api.service.ProductService;
import com.csu.mypetstore.api.util.ListBeanUtilsForPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final COSService cosService;
    private final ProductImageMapper productImageMapper;
    private final CategoryService categoryService;

    ProductServiceImpl(ProductMapper productMapper, CategoryMapper categoryMapper, COSService cosService, ProductImageMapper productImageMapper, CategoryService categoryService) {
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
        this.cosService = cosService;
        this.productImageMapper = productImageMapper;
        this.categoryService = categoryService;
    }

    @Override
    public CommonResponse<ProductDetailVO> getProductDetail(Integer pid) {
        Product product = productMapper.selectById(pid);

        if (product == null || product.getStatus() != CONSTANT.ProductStatus.ON_SALE.getCode())
            return CommonResponse.createResponseForError("商品不存在或已下架");

        Category category = categoryMapper.selectById(product.getCategoryId());
//        List<ProductImage> productImageList = productImageMapper.selectList(Wrappers.<ProductImage>query().eq("pid", pid));
        ProductDetailVO productDetailVO = ProductStructMapper.INSTANCE.product2DetailVO(product, getImageToken(pid, false), category.parentId());

        return CommonResponse.createResponseForSuccess(productDetailVO);
    }

    @Override
    public List<Map<String, Object>> getImageToken(Integer pid, boolean onlyMainImage) {
        return getImageToken(pid, onlyMainImage, true);
    }

    @Override
    public List<Map<String, Object>> getImageToken(Integer pid, boolean onlyMainImage, boolean withToken) {
        List<ProductImage> productImageList = productImageMapper.selectList(Wrappers.<ProductImage>query().eq("pid", pid));
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (ProductImage productImage: productImageList) {
            String imageId = productImage.getId();
            if (onlyMainImage && productImage.getType() != CONSTANT.ProductImageType.MAIN_IMAGE)
                break;  // 若只要求主图
            TencentCOSVO token = null;
            if (withToken)
                token = cosService.generatePolicy(imageId, CONSTANT.IMAGE_PERMISSION.GET_OBJECT).getData();
            Map<String, Object> map = new HashMap<>();
            map.put("image", productImage);
            map.put("token", token);
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public CommonResponse<Page<ProductListVO>> getProductList(Integer cid, String keyword, String orderBy, Boolean asc, int pageNum, int pageSize) {
        if (StringUtils.isBlank(keyword) && cid == null)
            return CommonResponse.createResponseForError(ResponseCode.ARGUMENT_ILLEGAL.getDescription(), ResponseCode.ARGUMENT_ILLEGAL.getCode());

        List<Category> categoryList = new ArrayList<>();
        List<Integer> categoryIdList = new ArrayList<>();
        if (cid != null) categoryList = categoryService.getALLChildCategoryList(cid);
        categoryList.forEach(category -> categoryIdList.add(category.id()));

        if (StringUtils.isBlank(keyword) && categoryIdList.isEmpty())
            return CommonResponse.createResponseForSuccess(new Page<>());

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
//        categoryList.forEach(item -> {
//            queryWrapper.eq("category_id", item.id());
//            queryWrapper.or();
//        });
        if (!categoryIdList.isEmpty())
            queryWrapper.in("category_id", categoryIdList);
        if (!StringUtils.isBlank(keyword)) {
            // TODO: 忽略大小写
//            queryWrapper.like("supper(name)", "supper("+keyword+")").or();
            queryWrapper.like("name", keyword).or();
            queryWrapper.like("subtitle", keyword).or();
            queryWrapper.like("detail", keyword);
        }
        if (!StringUtils.isBlank(orderBy) && CONSTANT.ORDER_BY_FIELD_LIST.contains(orderBy)) {
            if (asc)
                queryWrapper.orderByAsc(orderBy);
            else
                queryWrapper.orderByDesc(orderBy);
        }

        Page<Product> result = new Page<>(pageNum, pageSize);
        result = productMapper.selectPage(result, queryWrapper);  // 分页结果位于record属性中

        Page<ProductListVO> voResult = ListBeanUtilsForPage.copyPageProperties(
                result,
                ProductListVO::new,  // 大坑！final字段的属性不能被重复赋值，因此不能有无参构造器。故不能给属性加final关键字，也不能使用record类型。
                (product, productListVO) -> {
                    productListVO.setImageList(getImageToken(product.getId(), true));
                }
        );
        return CommonResponse.createResponseForSuccess(voResult);
    }


}
