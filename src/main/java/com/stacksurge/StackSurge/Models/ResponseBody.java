package com.stacksurge.StackSurge.Models;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class ResponseBody implements Serializable {
    String response;
    String error;
    boolean isSuccess;
    int statusCode;

    public ResponseBody(){
        response = null;
        error = null;
        isSuccess = true;
        statusCode = 200;
    }

    public String getError() {
        return error;
    }

    public String getResponse() {
        return response;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "{ Status Code: " + statusCode + ", Response: " + response + ", Error: " + error + ", Success: " + isSuccess + " }";
    }
}
