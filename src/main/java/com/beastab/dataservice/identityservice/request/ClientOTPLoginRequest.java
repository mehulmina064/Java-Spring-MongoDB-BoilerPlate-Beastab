package com.beastab.dataservice.identityservice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientOTPLoginRequest {
    private String otp;
    @JsonProperty("client_id")
    private String clientId;
}
