package com.hospital.registration.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @title: Result
 * @author: Su
 * @date: 2026/1/7 18:47
 * @version: 1.0
 * @description: TODO 请添加描述
 */
@Data
public class Result<T> {
    //是否成功
    private Boolean success;

    //返回码
    private Integer code;

    //返回消息
    private String message;

    //返回数据
    private Map<String, Object> data = new HashMap<String, Object>();

    //把构造方法私有
    private Result() {}

    //成功静态方法
    public static Result ok() {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        return result;
    }
    //成功：自己指定消息
    public static Result ok(String msg) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(msg);
        return result;
    }

    //成功：自己指定消息和返回code
    public static Result ok(Integer code, String msg) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

    //失败静态方法
    public static Result error() {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(ResultCode.FAIL.getCode());
        result.setMessage(ResultCode.FAIL.getMessage());
        return result;
    }
    //失败:自己指定失败消息
    public static Result error(String msg) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(ResultCode.FAIL.getCode());
        result.setMessage(msg);
        return result;
    }

    //失败：自己指定失败消息和code
    public static Result error(Integer code , String msg) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }
    //给返回消息添加数据
    public Result data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    //以下四个方法可以不关注，上面的方法已经基本满足需求
    public Result success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public Result message(String message){
        this.setMessage(message);
        return this;
    }

    public Result code(Integer code){
        this.setCode(code);
        return this;
    }

    public Result data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}
