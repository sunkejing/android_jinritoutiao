package com.ss.android.gamecommon.model;

public class ResultModel<T> {
    private T data;
    private String message;

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultModel() {

    }

    public ResultModel(T data, String message) {
        this.data = data;
        this.message = message;
    }
}
