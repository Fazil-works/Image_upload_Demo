package com.example.Stud.TeachWork.ResponseData;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor


public class ResponseData {

    private String status;
    private String message;
    private Object data;

    // Default constructor
    public ResponseData() {}

    // Constructor for success
    public ResponseData(String message, Object data) {
        this.message = message;
        this.data = data;
        this.status = "success";
    }

    // Constructor for failure
    public ResponseData(String message, String status) {
        this.message = message;
        this.status = status;
    }

    // Getters and setters
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}



