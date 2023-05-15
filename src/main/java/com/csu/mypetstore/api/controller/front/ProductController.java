package com.csu.mypetstore.api.controller.front;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.Product;
import com.csu.mypetstore.api.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("api/products/{pid}")
    public CommonResponse<Product> getProductDetail(@PathVariable Integer pid){
        return productService.getProductDetail(pid);
    }

    public CommonResponse<List<Product>> getProductList(
        @RequestParam(required = false) Integer cid,  // 要求递归查找
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "") String orderBy,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "2") int pageSize
    ) {
        // TODO: 完成逻辑
        return null;
    }
}
