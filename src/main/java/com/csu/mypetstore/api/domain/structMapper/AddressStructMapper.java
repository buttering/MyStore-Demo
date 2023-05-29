package com.csu.mypetstore.api.domain.structMapper;

import com.csu.mypetstore.api.domain.Address;
import com.csu.mypetstore.api.domain.vo.AddressVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressStructMapper {
    AddressStructMapper INSTANCE = Mappers.getMapper(AddressStructMapper.class);

    @Mapping(source = "id", target = "id")
    AddressVO address2VO(Address address);
}
