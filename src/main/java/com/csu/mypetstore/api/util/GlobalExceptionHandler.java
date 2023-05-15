package com.csu.mypetstore.api.util;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ConcurrentModificationException;
import java.util.List;

@ControllerAdvice  // 基于spring aop，截获控制器层的异常
@Slf4j  // 日志处理，能够取代下面对logger实例化的语句
public class GlobalExceptionHandler {
//    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //请求接口参数错误的异常处理
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public CommonResponse<Object> handleMissingParameterException(MissingServletRequestParameterException exception){
        return CommonResponse.createResponseForError( ResponseCode.ARGUMENT_ILLEGAL.getDescription(), ResponseCode.ARGUMENT_ILLEGAL.getCode());
    }

    // 封装的参数验证异常， 在controller中没有写result参数时抛出，对应@valid注解，错误信息是校验注解上的默认message
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)  // 改变HTTP响应的状态码
    @ResponseBody  // 将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，写入到response对象的body区，通常用来返回JSON数据或者是XML数据。
    public CommonResponse<String> argumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        log.error(e.getMessage());
        return CommonResponse.createResponseForError(
                formatValidErrorsMessage(e.getAllErrors()),
                ResponseCode.ARGUMENT_ILLEGAL.getCode()
        );
    }

    // 格式化MethodArgumentNotValidException的异常信息
    private String formatValidErrorsMessage(List<ObjectError> errors){
        StringBuffer errorMessage = new StringBuffer();
        errors.forEach(error -> errorMessage.append(error.getDefaultMessage()).append(";"));
        errorMessage.deleteCharAt(errorMessage.length() - 1);

        return errorMessage.toString();
    }

    // valid和validated注解的嵌套检验（类属性的属性）异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CommonResponse<String> methodConstraintViolationExceptionHandler(ConstraintViolationException e){
        log.error(e.getMessage());
        return CommonResponse.createResponseForError(
            e.getMessage()
        );
    }

    // mybatis异常
    @ExceptionHandler(MyBatisSystemException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CommonResponse<String> myBatisSystemExceptionHandler(MyBatisSystemException e){
        // Mybatis-plus在向数据库插入记录时，会返回一个主键值，并自动赋值到对应实体对象中。
        // 但是record类是只读的，因此会导致一个错误
        // 这个错误可以忽略。
        if (StringUtils.countMatches(e.getCause().getCause().getMessage(), "No setter found for the keyProperty") >= 1){
            return CommonResponse.createResponseForSuccess("注册用户成功");
        }

        log.error(e.getCause().getMessage());
        return CommonResponse.createResponseForError(
                "服务器异常了...",
                ResponseCode.ERROR.getCode()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResponse<String> exceptionHandler(Exception e){
        log.error(e.getMessage());
          return CommonResponse.createResponseForError(
                  "服务器异常了...",
                  ResponseCode.ERROR.getCode()
          );
    }

}
