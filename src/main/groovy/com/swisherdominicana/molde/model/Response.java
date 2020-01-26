package com.swisherdominicana.molde.model;

public class Response<T>  {
    private Integer statusCode = 1;
    private T data;
    private String message="";

    public Response(Integer statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public Response() {}

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
