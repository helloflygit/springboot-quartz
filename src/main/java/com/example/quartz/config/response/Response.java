package com.example.quartz.config.response;

import java.io.Serializable;

/**
 * 接口返回结果封装类
 *
 * @author hellofly
 * @date 2019/4/9
 */
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 676473785338095291L;

    private int status;

    private String message;

    private T data;

    public Response() {
    }

    public Response(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response(ResultEnum resultEnum) {
        this.status = resultEnum.getStatus();
        this.message = resultEnum.getMessage();
    }

    public Response(ResultEnum resultEnum, T data) {
        this.status = resultEnum.getStatus();
        this.message = resultEnum.getMessage();
        this.data = data;
    }

    public static Response error() {
        return new Response(ResultEnum.ERROR);
    }

    public static Response error(String errorMessage) {
        return new Response(ResultEnum.ERROR.getStatus(), errorMessage);
    }

    public static Response success() {
        return new Response(ResultEnum.SUCCESS);
    }

    public static <T> Response success(T data) {
        return new Response(ResultEnum.SUCCESS, data);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
