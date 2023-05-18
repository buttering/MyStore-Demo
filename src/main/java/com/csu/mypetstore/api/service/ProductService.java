package com.csu.mypetstore.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.vo.ProductDetailVO;
import com.csu.mypetstore.api.domain.vo.ProductListVO;

public interface ProductService {
    CommonResponse<ProductDetailVO> getProductDetail(Integer pid);

    CommonResponse<Page<ProductListVO>> getProductList(Integer cid, String keyword, String orderBy, Boolean asc, int pageNum, int pageSize);

}
