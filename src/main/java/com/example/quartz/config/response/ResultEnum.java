package com.example.quartz.config.response;

/**
 * 接口返回状态码及异常信息枚举类
 *
 * @author hellofly
 * @date 2019/4/9
 */
public enum ResultEnum {

    SUCCESS(0, "Success"),
    ERROR(-1, "Error");

    private int status;

    private String message;

    ResultEnum(int status, String message) {
        this.status = status;
        this.message = message;
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
}
