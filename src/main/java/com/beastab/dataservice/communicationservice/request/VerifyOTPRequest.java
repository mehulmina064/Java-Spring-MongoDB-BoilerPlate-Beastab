package com.beastab.dataservice.communicationservice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOTPRequest {
    private String otp;
    private String mobile;
    @JsonProperty("isd_code")
    private String isdCode;
}
