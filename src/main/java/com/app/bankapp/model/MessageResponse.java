package com.app.bankapp.model;

import org.springframework.http.HttpStatus;

public class MessageResponse<T> {
    private String message = "Successful";
    private int status = HttpStatus.OK.value();
    private boolean isSuccessful = true;
    private T data;


    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
