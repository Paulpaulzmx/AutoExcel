package com.z5n.autoexcel.model;

import lombok.Data;

@Data
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    public Result() {
    }

    public Result(Object data) {
        this.data = data;
    }

    public Result(String msg) {
        this.msg = msg;
    }

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result success() {
        return new Result(200, "success", null);
    }

    public static Result success(Object data) {
        return new Result(200, "success", data);
    }

    public static Result error() {
        return new Result(0, "error", null);
    }

    public static Result error(String msg) {
        return new Result(0, msg, null);
    }




}
