package com.csu.mypetstore.api;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.*;
import com.csu.mypetstore.api.domain.dto.PostOrderDTO;
import com.csu.mypetstore.api.domain.structMapper.ProductStructMapper;
import com.csu.mypetstore.api.domain.structMapper.UserStructMapper;
import com.csu.mypetstore.api.domain.dto.RegisterUserDTO;
import com.csu.mypetstore.api.domain.vo.ProductDetailVO;
import com.csu.mypetstore.api.domain.vo.ProductListVO;
import com.csu.mypetstore.api.domain.vo.TencentCOSVO;
import com.csu.mypetstore.api.persistence.*;

import com.csu.mypetstore.api.service.COSService;
import com.csu.mypetstore.api.service.CartService;
import com.csu.mypetstore.api.service.CategoryService;
import com.csu.mypetstore.api.service.OrderService;
import com.csu.mypetstore.api.service.impl.ProductServiceImpl;
import com.csu.mypetstore.api.util.ValidGroup;
import com.github.benmanes.caffeine.cache.Cache;
import com.tencent.cloud.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Supplier;

@SpringBootTest

class MyPetstoreApplicationTests {

    @Autowired
    AddressMapper addressMapper;
    @Autowired
    CartItemMapper cartMapper;
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
    @Autowired
    COSService cosService;
    @Autowired
    ProductImageMapper productImageMapper;
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    Cache<String, String> localCache;
    @Autowired
    Cache<String, ImageToken> imageTokenCache;
    @Autowired
    CartService cartService;
    @Autowired
    OrderService orderService;

    @Value("${tencent-cloud.cos.bucket}")
    String bucket;

    @Test
    void contextLoads() {
        System.out.println("context load");
    }

    @Test
    void myBatis(){
        List<Address> addressList = addressMapper.selectList(null);
        List<CartItem> cartList = cartMapper.selectList(null);
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

        RegisterUserDTO registerUserDTO = UserStructMapper.INSTANCE.user2RegisterDTO(user);

        System.out.println(user);
        System.out.println(registerUserDTO);
    }

    @Test
    void testMultiplyMapStruct(){
        Product product = productMapper.selectById("10");

        ProductDetailVO productDetailVO = ProductStructMapper.INSTANCE.product2DetailVO(product,2);

        System.out.println(product);
        System.out.println(productDetailVO);
    }

    @Test
    void testUpdate(){
        User user = userMapper.selectOne(Wrappers.<User>query().eq("id", 2));
        System.out.println(user);

//        user = user.withAnswer("LEN");
//        userMapper.updateById(user);

//        user = user.withAnswer("RIN");
//        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.eq("id", 3);
//        userMapper.update(user, updateWrapper);

        UpdateWrapper<User> updateWrapper = Wrappers.<User>update().eq("id", 2).set("answer", "LUKA");
        userMapper.update(null, updateWrapper);

        user = userMapper.selectOne(Wrappers.<User>query().eq("id", 2));
        System.out.println(user);
    }

    @Test
    void configurationParamTest(){
        System.out.println(bucket);
    }

    @Test
    void tencentCOSTest() {
        CommonResponse<?> commonResponse = cosService.generatePolicy("1", CONSTANT.IMAGE_PERMISSION.GET_OBJECT);
        Response response = (Response) commonResponse.getData();
        System.out.println(response.credentials.tmpSecretId);
        System.out.println(response.credentials.tmpSecretKey);
        System.out.println(response.credentials.sessionToken);
    }

    @Test
    void testInsert() {

        Category category = new Category(7, 6, "banana", CONSTANT.ProductStatus.ON_SALE.getCode(), null, LocalDateTime.now(), LocalDateTime.now());
        categoryMapper.insert(category);
    }

    @Test
    void testChildrenCategory() {
        List<Category> categoryList = categoryService.getALLChildCategoryList(4);
        System.out.println(categoryList);
    }

    @Test
    void testRecordCopy() {
//        List<Product> source = productMapper.selectList(Wrappers.<Product>query().eq("id", 1));
//
//        List<ProductListVO> target = ListBeanUtils.copyListProperties(source, ProductListVO::new, (a, b)-> {
//            return;
//        });
//        System.out.println(target);
    }

    @Test
    void testFunctional() {
        Supplier<ProductListVO> supplier = ProductListVO::new;
        ProductListVO product = supplier.get();
        System.out.println(product);
    }

    @Test
    void testCache() {
        ImageToken imageToken = new ImageToken("1", "sdfasdfsdf", CONSTANT.IMAGE_PERMISSION.GET_OBJECT, LocalDateTime.now());
        localCache.put("aaa", "aaa");
        localCache.put("aaa", "bbb");
        imageTokenCache.put("aaa", imageToken);

        System.out.println(localCache.getIfPresent("aaa"));
        System.out.println(imageTokenCache.getIfPresent("aaa"));
    }

    @Value("E:\\project\\myPetstore\\src\\main\\resources\\static\\AlipayRSAPrivateKey.txt")
    String path;
    @Test
    void testFile() throws IOException {
        String privateKey = Files.readString(Paths.get(path));
        System.out.println(privateKey);
    }

    Integer validated(@Validated PostOrderDTO postOrderDTO) {
        System.out.println(postOrderDTO);
        return 1;
    }

    Integer validatedGroup(@Validated(ValidGroup.CreateOrderWithProductId.class) PostOrderDTO postOrderDTO) {
        System.out.println(postOrderDTO);
        return 2;
    }
    @Test
    void testValid() {
        PostOrderDTO postOrderDTO = new PostOrderDTO(1, 1 , 2);
        validated(postOrderDTO);validatedGroup(postOrderDTO);

        postOrderDTO = new PostOrderDTO(1, 1, 0);
        validated(postOrderDTO);validatedGroup(postOrderDTO);

        postOrderDTO  = new PostOrderDTO(1, null, null);
        validated(postOrderDTO);validatedGroup(postOrderDTO);

        postOrderDTO = new PostOrderDTO(null, null, null);
        validated(postOrderDTO);validatedGroup(postOrderDTO);

        orderService.createOrder(1,1,1,1);
        orderService.createOrder(1,1,1,0);
    }

    @Test
    void testLock() {
        Product product = productMapper.selectById(10);
        Product product1 = productMapper.selectById(10);
        System.out.println(product);

        product.setName("aaaaaa");
        product1.setName("bbbbbb");

        System.out.println(productMapper.updateById(product));
        System.out.println(productMapper.updateById(product1));
    }
}
