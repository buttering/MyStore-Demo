package com.csu.mypetstore.api.common;

public final class CONSTANT {
    // 将产量置于接口中的好处
    // 1. 整洁，默认修饰符public static final
    // 2. 可在多个文件中使用
    // 3. 不会有任何开销
    public interface ROLE{
        int CUSTOMER = 1;
        int ADMIN = 0;
    }
}
