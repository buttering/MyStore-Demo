package com.csu.mypetstore.api.common;

import java.util.HashMap;

public final class CONSTANT {
    // 将常量置于接口中的好处
    // 1. 整洁，默认修饰符public static final
    // 2. 可在多个文件中使用
    // 3. 不会有任何开销
    public interface ROLE {
        int CUSTOMER = 1;
        int ADMIN = 0;
    }

    public static final String LOGIN_USER = "loginUser";

    public static final HashMap<String, String> REGISTER_FIELD_MAP = new HashMap<>();

    public interface CHECK_FIELD {
        String USERNAME = "username";
        String PASSWORD = "password";
        String EMAIL = "email";
    }

    public CONSTANT(){
        REGISTER_FIELD_MAP.put("username", "用户名");
        REGISTER_FIELD_MAP.put("phone", "电话号码");
        REGISTER_FIELD_MAP.put("email", "电子邮件");
    }
}
