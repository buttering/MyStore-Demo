package com.csu.mypetstore.api.controller.front;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.Product;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {
    public CommonResponse<List<Product>> getProductList(){
        // TODO: 完成逻辑
        return null;
    }

    public CommonResponse<Object> getProductList(
        @RequestParam(required = false) Integer categoryId,  // 要求递归查找
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "") String orderBy,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "2") int pageSize
    ) {
        // TODO: 完成逻辑
        return null;
    }
}
