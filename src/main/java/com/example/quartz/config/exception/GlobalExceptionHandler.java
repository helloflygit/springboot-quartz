package com.example.quartz.config.exception;

import com.example.quartz.config.response.Response;
import com.example.quartz.config.response.ResultEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局异常处理
 *
 * @author hellofly
 * @date 2019/4/11
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);


    /**
     * BindingResult参数校验
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public Response handleBindException(BindException e) {
        String bindingResultError = e.getBindingResult().getFieldError().getDefaultMessage();
        String errorMessage = StringUtils.isEmpty(bindingResultError) ? ResultEnum.ERROR.getMessage() : bindingResultError;
        return Response.error(errorMessage);
    }

    /**
     * 全局异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        logger.error("Exception happened: ", e);
        String errorMessage = StringUtils.isEmpty(e.getMessage()) ? ResultEnum.ERROR.getMessage() : e.getMessage();
        return Response.error(errorMessage);
    }

}