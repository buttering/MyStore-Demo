package com.csu.mypetstore.api.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)  // 控制序列化,如果加该注解的字段为null,那么就不序列化这个字段。
public class CommonResponse <T> {
    private final int code;
    private final T data;
    private final String message;

    private CommonResponse(int code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 用于后端层与层之间判断响应是否成功
    @JsonIgnore  // 不被序列化
    public boolean isSuccess(){
        return this.code == ResponseCode.SUCCESS.getCode();
    }

    public static <A> CommonResponse<A> createResponseForSuccess(){
        return new CommonResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDescription(), null);
    }


    public static <A> CommonResponse<A> createResponseForSuccess(A data){
        return new CommonResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDescription(), data);
    }

    public static <A>CommonResponse<A> createResponseForError(){
        return new CommonResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDescription(), null);
    }

    public static <A>CommonResponse<A> createResponseForError(String message){
        return new CommonResponse<>(ResponseCode.ERROR.getCode(), message, null);
    }

    public static <A>CommonResponse<A> createResponseForError(String message, Integer code){
        return new CommonResponse<>(code, message, null);
    }

}
