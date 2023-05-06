package com.csu.mypetstore.api;

import com.csu.mypetstore.api.domain.User;
import com.csu.mypetstore.api.persistence.UserMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MyPetstoreApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        System.out.println("context load");
    }

    @Test
    void myBatis(){
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

}
