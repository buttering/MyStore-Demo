package com.csu.mypetstore.api.controller.front;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.Product;
import com.csu.mypetstore.api.domain.ProductImage;
import com.csu.mypetstore.api.domain.structMapper.ProductStructMapper;
import com.csu.mypetstore.api.domain.vo.ProductDetailVO;
import com.csu.mypetstore.api.domain.vo.TencentCOSVO;
import com.csu.mypetstore.api.persistence.ProductMapper;
import com.csu.mypetstore.api.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    private final ProductService productService;

    private final ProductMapper productMapper;

    ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping("api/products/{pid}")
    public CommonResponse<ProductDetailVO> getProductDetail(@PathVariable Integer pid) {
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
