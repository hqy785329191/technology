package com.shop.manage.system.support;


import java.io.Serializable;

/**
 * @author Mr.joey
 */
public class Response<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Response<T> ok(T data) {
        int okCode = 200;
        return new Response<>(okCode, "请求成功", data);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
