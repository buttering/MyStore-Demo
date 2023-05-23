package com.csu.mypetstore.api.util;

import com.csu.mypetstore.api.domain.vo.CartItemVO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ListBeanUtils extends BeanUtils {
    public static <S, T> List<T> copyListProperties(List<S> sourceList, Supplier<T> targetSupplier, BiConsumer<S, T> callback) {
        List<T> targetList = new ArrayList<>(sourceList.size());
        for (S source: sourceList) {
            T target = targetSupplier.get();  // 若传入无参构造方法，这里会被实例化
            copyProperties(source, target);  // 父类方法，进行浅拷贝
            if (callback != null) {
                callback.accept(source, target);
            }
            targetList.add(target);
        }
        return targetList;
    }
}
