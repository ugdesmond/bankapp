package com.app.bankapp.model;

import java.util.Map;

public class ExceptionResponse {
    private String status;
    private int code;
    private String path;
    private String message;
    private String timeStamp;


    public ExceptionResponse(int status, Map<String, Object> errorAttributes) {
        this.setStatus("failure");
        this.setPath((String) errorAttributes.get("path"));
        this.setMessage((String) errorAttributes.get("message"));
        this.setTimeStamp(errorAttributes.get("timestamp").toString());
        this.setCode(status);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    private void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }



    public String getPath() {
        return path;
    }

    private void setPath(String path) {
        this.path = path;
    }
}
