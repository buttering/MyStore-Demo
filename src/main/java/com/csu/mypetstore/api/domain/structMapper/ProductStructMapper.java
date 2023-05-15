package com.csu.mypetstore.api.domain.structMapper;

import com.csu.mypetstore.api.domain.Product;
import com.csu.mypetstore.api.domain.vo.ProductDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductStructMapper {
    ProductStructMapper INSTANCE = Mappers.getMapper(ProductStructMapper.class);

    ProductDetailVO product2DetailVO(Product product, Integer parentCategoryId, String imageServer);
}
