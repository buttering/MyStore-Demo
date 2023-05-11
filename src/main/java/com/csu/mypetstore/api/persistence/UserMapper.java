package com.csu.mypetstore.api.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csu.mypetstore.api.domain.User;
import org.springframework.stereotype.Component;

@Component  // 标记为Spring容器的Bean，取消IED标红
public interface UserMapper extends BaseMapper<User> {

}

