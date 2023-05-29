package com.csu.mypetstore.api.exception;

public class APIException extends RuntimeException{
    public APIException(String msg) {
        super(msg);
    }
}
