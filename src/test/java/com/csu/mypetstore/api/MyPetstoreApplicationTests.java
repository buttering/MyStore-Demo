package com.csu.mypetstore.api;

import com.csu.mypetstore.api.domain.*;
import com.csu.mypetstore.api.persistence.*;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@MapperScan("com.csu.mypetstore.api.persistence")
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

}
