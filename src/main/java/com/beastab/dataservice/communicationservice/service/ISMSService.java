package com.beastab.dataservice.communicationservice.service;

import com.beastab.dataservice.communicationservice.request.VerifyOTPRequest;
import com.beastab.dataservice.communicationservice.response.OTPResponse;
import com.beastab.dataservice.communicationservice.response.VerifyOTPResponse;
import org.springframework.http.ResponseEntity;

public interface ISMSService {
    public ResponseEntity<OTPResponse> sendOTP(String mobile, String isdCode);
    public ResponseEntity<VerifyOTPResponse> verifyOTP(VerifyOTPRequest otpRequest);
}
