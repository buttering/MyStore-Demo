package com.csu.mypetstore.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import com.csu.mypetstore.api.domain.Category;
import com.csu.mypetstore.api.domain.structMapper.CategoryStructMapper;
import com.csu.mypetstore.api.domain.vo.CategoryVO;
import com.csu.mypetstore.api.persistence.CategoryMapper;
import com.csu.mypetstore.api.service.CategoryService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }


    @Override
    public CommonResponse<CategoryVO> getCategoryById(Integer categoryId) {
        if (categoryId == null)
            return CommonResponse.createResponseForError("分类ID不能为空");
        if (categoryId == CONSTANT.CATEGORY_ROOT)
            return CommonResponse.createResponseForError("根分类无信息");

        Category category = categoryMapper.selectById(categoryId);
        if (category == null)
            return CommonResponse.createResponseForError(ResponseCode.EMPTY_OBJECT.getDescription(), ResponseCode.EMPTY_OBJECT.getCode());

        CategoryVO categoryVO= CategoryStructMapper.INSTANCE.category2VO(category);
        return CommonResponse.createResponseForSuccess(categoryVO);
    }

    @Override
    public CommonResponse<List<CategoryVO>> getChildCategoryList(Integer categoryId) {
        if (categoryId == null)
            return CommonResponse.createResponseForError("分类ID不能为空");

        List<Category> categoryList = categoryMapper.selectList(Wrappers.<Category>query().eq("parent_id", categoryId));

        List<CategoryVO> categoryVOList = Lists.newArrayList();
        categoryList.forEach(category ->
                categoryVOList.add(CategoryStructMapper.INSTANCE.category2VO(category))
        );

        return CommonResponse.createResponseForSuccess(categoryVOList);
    }



    @Override
    public List<Category> getALLChildCategoryList(Integer categoryId) {
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
        return categoryList;

    }

    @Override
    public CommonResponse<List<Integer>> getAllChildCategoryIdList(Integer categoryId) {
        if (categoryId == null)
            return CommonResponse.createResponseForError("分类ID不能为空");

        List<Category> categoryList = getALLChildCategoryList(categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();

        categoryList.forEach(category ->
            categoryIdList.add(category.id())
        );

        return CommonResponse.createResponseForSuccess(categoryIdList);

    }
}
