package com.csu.mypetstore.api;

import com.csu.mypetstore.api.domain.*;
import com.csu.mypetstore.api.dto.dtoMapper.UserDTOMapper;
import com.csu.mypetstore.api.dto.RegisterUserDTO;
import com.csu.mypetstore.api.persistence.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;

@SpringBootTest

class MyPetstoreApplicationTests {

    @Autowired
    AddressMapper addressMapper;
    @Autowired
    CartMapper cartMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    PayInfoMapper payInfoMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {
        System.out.println("context load");
    }

    @Test
    void myBatis(){
        List<Address> addressList = addressMapper.selectList(null);
        List<Cart> cartList = cartMapper.selectList(null);
        List<Category> categoryList = categoryMapper.selectList(null);
        List<OrderItem> orderItemList = orderItemMapper.selectList(null);
        List<PayInfo> payInfoList = payInfoMapper.selectList(null);
        List<Product> productList = productMapper.selectList(null);
        List<User> userList = userMapper.selectList(null);

        addressList.forEach(System.out::println);
        cartList.forEach(System.out::println);
        categoryList.forEach(System.out::println);
        orderItemList.forEach(System.out::println);
        payInfoList.forEach(System.out::println);
        productList.forEach(System.out::println);
        userList.forEach(System.out::println);
    }

    @Test
    void testTimeFormat(){
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'+08:00'");
        System.out.println(zdt);
        System.out.println(dtf.format(zdt));

        TimeZone tzNY = TimeZone.getTimeZone("America/New_York");
    }

    @Test
    void testMapStruct(){
        User user = new User(1, "wang", "pwd", "", "", "1", "2", 1, null, null);

        RegisterUserDTO registerUserDTO = UserDTOMapper.INSTANCE.user2RegisterUserDTO(user);

        System.out.println(user);
        System.out.println(registerUserDTO);
    }
}
