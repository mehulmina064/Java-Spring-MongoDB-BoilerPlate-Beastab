package com.beastab.dataservice.communicationservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OTPRequest {
    private String ISDCode;
    private String mobile;
    private boolean isRegistration;
}
