package com.csu.mypetstore.api.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public interface CategoryService {
    public CommonResponse<List<Category>> getCategoryList(Integer categoryId);

    public CommonResponse<List<Category>> getChildCategoryList(Integer categoryId);
}
