package com.bread.traveler.vo;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

@Data
public class Result {

    private int code;
    private String message;
    private Object data;

    public Result(){}

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success(Object data){
        return new Result(HttpServletResponse.SC_OK, "success", data);
    }

    public static Result success(String message, Object data){
        return new Result(HttpServletResponse.SC_OK, message, data);
    }

    public static Result serverError(String message){
        return new Result(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, null);
    }

    public static Result businessError(String message){
        return new Result(HttpServletResponse.SC_BAD_REQUEST, message, null);
    }

    public static Result businessError(int code, String message){
        return new Result(code, message, null);
    }

}
