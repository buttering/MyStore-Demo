package com.csu.mypetstore.api.service;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.Product;

import java.util.List;

public interface ProductService {
    CommonResponse<Product> getProductDetail(Integer pid);

    CommonResponse<List<Product>> getProductList(Integer cid, String keyword, String orderBy, int pageNum, int pageSize);

}
