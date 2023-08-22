package com.beastab.dataservice.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseRestResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private BasePaginationResponse paginationResponse;

    public BaseRestResponse(int statusCode, String message, T data, BasePaginationResponse paginationResponse) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.paginationResponse = paginationResponse;
    }

    public BaseRestResponse() {

    }
}
