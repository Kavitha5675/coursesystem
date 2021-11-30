package com.personal.courseproject.coursesystem.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ErrorResponse {
    @JsonProperty
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public ErrorResponse(){}
}
