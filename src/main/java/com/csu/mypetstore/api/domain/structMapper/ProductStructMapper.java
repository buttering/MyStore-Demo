package com.csu.mypetstore.api.domain.structMapper;

import com.csu.mypetstore.api.domain.Product;
import com.csu.mypetstore.api.domain.vo.CartItemVO;
import com.csu.mypetstore.api.domain.vo.ProductDetailVO;
import com.csu.mypetstore.api.domain.vo.ProductListVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductStructMapper {
    ProductStructMapper INSTANCE = Mappers.getMapper(ProductStructMapper.class);

//    ProductDetailVO product2DetailVO(Product product, List<ProductDetailVO.ImageToken> imageList, Integer parentCategoryId);

//    ProductDetailVO product2DetailVO(Product product, ProductDetailVO.ImageToken imageToken, Integer parentCategoryId);

//    @Mapping(source = "productImage", target = "image")
//    @Mapping(source = "product.id", target = "id")
//    @Mapping(source = "tencentCOSVO", target = "token")
//    @Mapping(source = "product.createTime", target = "createTime")
//    ProductDetailVO product2DetailVO(Product product, TencentCOSVO tencentCOSVO, ProductImage productImage, Integer parentCategoryId);

    @Mapping(source = "imageTokenList", target = "imageList")
    ProductDetailVO product2DetailVO(Product product, List<Map<String, Object>> imageTokenList, Integer parentCategoryId);

    ProductDetailVO product2DetailVO(Product product, Integer parentCategoryId);

    ProductListVO product2ListVO(Product product);

    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.subtitle", target = "productSubtitle")
    @Mapping(source = "product.price", target = "productPrice")
    @Mapping(source = "product.stock", target = "productStock")
    @Mapping(source = "cartItemVO.id", target = "id")
    @Mapping(source = "cartItemVO.selected", target = "selected")
    CartItemVO product2CartItemVO(CartItemVO cartItemVO, Product product);
}
