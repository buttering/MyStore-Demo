package com.csu.mypetstore.api.util;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ConcurrentModificationException;
import java.util.List;

@ControllerAdvice  // 基于spring aop，截获控制器层的异常
@Slf4j  // 日志处理
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 封装的参数验证异常， 在controller中没有写result参数时抛出，对应@valid注解，错误信息是校验注解上的默认message
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)  // 改变HTTP响应的状态码
    @ResponseBody  // 将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，写入到response对象的body区，通常用来返回JSON数据或者是XML数据。
    public CommonResponse<String> argumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        logger.error(e.getMessage());
        return CommonResponse.createResponseForError(
                formatValidErrorsMessage(e.getAllErrors()),
                ResponseCode.ARGUMENT_ILLEGAL.getCode()
        );
    }

    // valid和validated注解的嵌套检验（类属性的属性）异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CommonResponse<String> methodConstraintViolationExceptionHandler(ConstraintViolationException e){
        logger.error(e.getMessage());
        return CommonResponse.createResponseForError(
            e.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResponse<String> exceptionHandler(Exception e){
        logger.error(e.getMessage());
        return CommonResponse.createResponseForError("服务器异常了...");
    }

    // 格式化MethodArgumentNotValidException的异常信息
    private String formatValidErrorsMessage(List<ObjectError> errors){
        StringBuffer errorMessage = new StringBuffer();
        errors.forEach(error -> errorMessage.append(error.getDefaultMessage()).append(";"));
        errorMessage.deleteCharAt(errorMessage.length() - 1);

        return errorMessage.toString();
    }
}