package com.csu.mypetstore.api.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ListBeanUtilsForPage {
    public static <S, T> Page<T> copyPageProperties(Page<S> sourcePage, Supplier<T> targetSupplier, BiConsumer<S, T> callback) {
        Page<T> page = new Page<>();
        page.setTotal(sourcePage.getTotal());
        page.setSize(sourcePage.getSize());
        page.setCurrent(sourcePage.getCurrent());
        List<S> sourceList = sourcePage.getRecords();
        List<T> targetList = ListBeanUtils.copyListProperties(sourceList, targetSupplier, callback);

        page.setRecords(targetList);
        return page;
    }
}
