package com.example.democoindeskapi.utils;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ErrorResponse {

    private String status;
    private String error;
    private String errorType;

    public ErrorResponse(String status, String error, String errorType) {
        super();
        this.error = error;
        this.status = status;
        this.errorType = errorType;
    }

    public ErrorResponse() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getError() {
        return error;
    }

    public String getStatus() {
        return status;
    }

    public String getErrorType() {
        return errorType;
    }

}



