package com.stacksurge.StackSurge.utility;

import org.springframework.stereotype.Component;

@Component
public class ResponseBody {
    String response;
    String error;
    boolean isSuccess;

    public ResponseBody(){
        response = null;
        error = null;
        isSuccess = true;
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
}
