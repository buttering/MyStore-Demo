package com.csu.mypetstore.api.domain.structMapper;

import com.csu.mypetstore.api.domain.CartItem;
import com.csu.mypetstore.api.domain.OrderItem;
import com.csu.mypetstore.api.domain.Product;
import com.csu.mypetstore.api.domain.vo.CartItemVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CartItemStructMapper {
    CartItemStructMapper INSTANCE = Mappers.getMapper(CartItemStructMapper.class);


    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.subtitle", target = "productSubtitle")
    @Mapping(source = "product.price", target = "productPrice")
    @Mapping(source = "product.stock", target = "productStock")
    @Mapping(source = "cartItemVO.id", target = "id")
    @Mapping(source = "cartItemVO.selected", target = "selected")
    CartItemVO product2CartItemVO(CartItemVO cartItemVO, Product product);
}
