package com.csu.mypetstore.api.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.Product;
import com.csu.mypetstore.api.domain.vo.ProductDetailVO;
import com.csu.mypetstore.api.domain.vo.ProductListVO;
import com.csu.mypetstore.api.persistence.ProductMapper;
import com.csu.mypetstore.api.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("api/products")
    public CommonResponse<Page<ProductListVO>> getProductList(
            @RequestParam(required = false) Integer cid,  // 要求递归查找
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "") String order,
            @RequestParam(defaultValue = "false") Boolean asc,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        // TODO: 完成逻辑
        return productService.getProductList(cid, keyword, order, asc, page, size);
    }
}
