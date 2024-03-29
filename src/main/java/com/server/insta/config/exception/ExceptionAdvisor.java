package com.server.insta.config.exception;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.sql.SQLException;

import static com.server.insta.config.exception.BusinessExceptionStatus.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvisor {

    private final ResponseService responseService;

    /**
     * javax.validation.Valid로 지정한 형식에 맞지않을경우 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult processValidationError(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        return responseService.getFailResult("VALID",builder.toString());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult customException(BusinessException e) {
        return responseService.getFailResult(e.getStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult NotFoundedException(){
        return responseService.getFailResult(NOT_FOUND);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult NotSupportedException(){
        return responseService.getFailResult(METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(SQLException.class)
    public CommonResult DataBaseException(){
        return responseService.getFailResult(DATABASE_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult commonException() {
        return responseService.getFailResult(SERVER_ERROR);
    }



}
