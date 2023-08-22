package com.beastab.dataservice.communicationservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Msg91SendOTPResponse {
    @JsonProperty("request_id")
    private String requestId;
    private String type;
}
