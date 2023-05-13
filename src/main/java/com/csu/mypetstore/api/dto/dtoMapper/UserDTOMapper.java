package com.csu.mypetstore.api.dto.dtoMapper;

import com.csu.mypetstore.api.domain.User;
import com.csu.mypetstore.api.dto.RegisterUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserDTOMapper {
    UserDTOMapper INSTANCE = Mappers.getMapper(UserDTOMapper.class);

    RegisterUserDTO user2RegisterUserDTO(User user);

    User registerUserDTO2User(RegisterUserDTO registerUserDTO);
}
