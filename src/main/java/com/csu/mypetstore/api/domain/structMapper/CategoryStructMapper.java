package com.csu.mypetstore.api.domain.structMapper;

import com.csu.mypetstore.api.domain.Category;
import com.csu.mypetstore.api.domain.vo.CategoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryStructMapper {
    CategoryStructMapper INSTANCE = Mappers.getMapper(CategoryStructMapper.class);

    CategoryVO category2VO(Category category);
}
