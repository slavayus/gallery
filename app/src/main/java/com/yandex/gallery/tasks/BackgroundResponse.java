package com.yandex.gallery.tasks;

/**
 * Created by slavik on 4/16/18.
 */

public class BackgroundResponse<T> {
    private T data;
    private BackgroundStatus status;
    private String message;

    BackgroundResponse(BackgroundStatus status) {
        this.status = status;
    }

    BackgroundResponse addMessage(String message) {
        this.message = message;
        return this;
    }

    BackgroundResponse addData(T data) {
        this.data = data;
        return this;
    }

    public T getData() {
        return data;
    }

    public BackgroundStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
