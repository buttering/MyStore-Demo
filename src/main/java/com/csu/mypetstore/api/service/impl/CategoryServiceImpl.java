package com.csu.mypetstore.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.Category;
import com.csu.mypetstore.api.persistence.CategoryMapper;
import com.csu.mypetstore.api.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CommonResponse<List<Category>> getCategoryList(Integer categoryId) {
        return null;
    }

    @Override
    public CommonResponse<List<Category>> getChildCategoryList(Integer categoryId) {
        List<Category> categoryList = new ArrayList<>();
        Stack<Category> categoryStack = new Stack<>();

        List<Category> currentCategoryList =  categoryMapper.selectList(Wrappers.<Category>query().eq("id", categoryId));
        currentCategoryList.forEach(categoryStack::push);

        while (!categoryStack.isEmpty()) {
            Category category = categoryStack.pop();
            categoryList.add(category);

            currentCategoryList = categoryMapper.selectList(Wrappers.<Category>query().eq("parent_id", category.id()));
            currentCategoryList.forEach(categoryStack::push);
        }
        return CommonResponse.createResponseForSuccess(categoryList);

    }
}
