package com.beastab.dataservice.communicationservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Msg91VerifyOTPResponse {
    private String message;
    private String type;
}
