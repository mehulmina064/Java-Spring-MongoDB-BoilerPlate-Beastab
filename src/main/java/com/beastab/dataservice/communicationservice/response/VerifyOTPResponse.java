package com.beastab.dataservice.communicationservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOTPResponse {
    private String message;
    private String type;

    public VerifyOTPResponse(Msg91VerifyOTPResponse response){
        this.message = response.getMessage();
        this.type = response.getType();
    }
}
