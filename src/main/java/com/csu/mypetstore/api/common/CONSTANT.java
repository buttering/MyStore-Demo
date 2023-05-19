package com.csu.mypetstore.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

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
    static {
        REGISTER_FIELD_MAP.put("username", "用户名");
        REGISTER_FIELD_MAP.put("phone", "电话号码");
        REGISTER_FIELD_MAP.put("email", "电子邮件");
    }

    public interface CHECK_FIELD {
        String USERNAME = "username";
        String PASSWORD = "password";
        String EMAIL = "email";
    }

    @Getter
    @AllArgsConstructor
    public enum ProductStatus {

        ON_SALE(1, "on_sale"),
        TAKE_DOWN(2, "take_down"),
        DELETE(3, "delete");

        private final int code;
        private final String description;
    }

    public interface ProductImageType {
        int MAIN_IMAGE = 1;
        int SUB_IMAGE = 2;
    }

    public static final Set<String> ORDER_BY_FIELD_LIST = new HashSet<>();
    static {
        ORDER_BY_FIELD_LIST.add("category_id");
        ORDER_BY_FIELD_LIST.add("name");
        ORDER_BY_FIELD_LIST.add("price");
        ORDER_BY_FIELD_LIST.add("stock");
    }

    public interface CART_ITEM_STATUS {
        int CHECKED = 1;
        int UNCHECKED = 0;
    }

    @Getter
    @AllArgsConstructor
    public enum IMAGE_PERMISSION {
        GET_OBJECT("name/cos:GetObject");

        private final String permission;

    }
}
