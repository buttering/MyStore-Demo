package com.csu.mypetstore.api.util;

import com.csu.mypetstore.api.domain.vo.CartItemVO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ListBeanUtils extends BeanUtils {
    // 回调函数修改其参数
    public static <S, T> List<T> copyListProperties(List<S> sourceList, Supplier<T> targetConstructor, BiConsumer<S, T> callback) {
        List<T> targetList = new ArrayList<>(sourceList.size());
        for (S source: sourceList) {
            T target = targetConstructor.get();  // 若传入无参构造方法，这里会被实例化
            copyProperties(source, target);  // 父类方法，进行浅拷贝
            if (callback != null)
                callback.accept(source, target);

            targetList.add(target);
        }
        return targetList;
    }

    // 回调函数会对目标对象重新赋值
    public static <S, T> List<T> copyListProperties(List<S> sourceList, Supplier<T> tartConstructor, BiFunction<S, T, T> callback) {
        List<T> targetList = new ArrayList<>(sourceList.size());
        for (S source: sourceList) {
            T target = tartConstructor.get();
            copyProperties(source, target);
            if (callback != null)
                target = callback.apply(source, target);

            targetList.add(target);
        }
        return targetList;
    }
}
