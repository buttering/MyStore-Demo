package com.csu.mypetstore.api.domain.structMapper;

import com.csu.mypetstore.api.domain.User;
import com.csu.mypetstore.api.domain.dto.RegisterUserDTO;
import com.csu.mypetstore.api.domain.vo.UserInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserStructMapper {
    UserStructMapper INSTANCE = Mappers.getMapper(UserStructMapper.class);

    RegisterUserDTO user2RegisterDTO(User user);

    User registerDTO2User(RegisterUserDTO registerUserDTO);

    UserInfoVO user2InfoVO(User user);
}
