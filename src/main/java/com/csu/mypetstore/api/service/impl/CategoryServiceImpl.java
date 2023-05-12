package com.csu.mypetstore.api.service.impl;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.Category;
import com.csu.mypetstore.api.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    @Override
    public CommonResponse<List<Category>> getCategoryList(Integer categoryId) {
        return null;
    }

    @Override
    public CommonResponse<List<Category>> getChildCategoryList(Integer categoryId) {
        return null;
    }
}
