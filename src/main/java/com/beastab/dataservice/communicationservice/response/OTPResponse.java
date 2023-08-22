package com.beastab.dataservice.communicationservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPResponse {
    @JsonProperty("type")
    private String type;
    private String message;

    public OTPResponse(Msg91SendOTPResponse thirdPartyResponse){
        this.type = thirdPartyResponse.getType();
        this.message = thirdPartyResponse.getRequestId();
    }
}
