package com.csu.mypetstore.api.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csu.mypetstore.api.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper

public interface UserMapper extends BaseMapper<User> {

}

