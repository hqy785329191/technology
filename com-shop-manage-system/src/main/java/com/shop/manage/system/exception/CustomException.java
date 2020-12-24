package com.shop.manage.system.exception;

/**
 * @author Mr.joey
 */
public class CustomException extends Exception {

    /**
     * 状态码
     */
    private int code;

    /**
     * 提示信息
     */
    private String msg;


    public CustomException() {
    }

    public CustomException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
