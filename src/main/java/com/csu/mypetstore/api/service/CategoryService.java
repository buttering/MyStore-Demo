package com.csu.mypetstore.api.service;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.Category;

import java.util.List;

public interface CategoryService {
    public CommonResponse<List<Category>> getCategoryList(Integer categoryId);

    public CommonResponse<List<Category>> getChildCategoryList(Integer categoryId);
}
