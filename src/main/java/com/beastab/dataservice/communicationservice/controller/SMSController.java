package com.beastab.dataservice.communicationservice.controller;

import com.beastab.dataservice.communicationservice.request.OTPRequest;
import com.beastab.dataservice.communicationservice.request.VerifyOTPRequest;
import com.beastab.dataservice.communicationservice.response.OTPResponse;
import com.beastab.dataservice.communicationservice.response.VerifyOTPResponse;
import com.beastab.dataservice.communicationservice.service.ISMSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Communication Service")
@RestController
@RequestMapping("/api/v1/sms")
public class SMSController {

    @Autowired
    private ISMSService smsService;

    @Operation(summary = "send otp without user details", method = "POST")
    @PostMapping("/send-otp")
    public ResponseEntity<OTPResponse> sendOTP(@RequestBody OTPRequest otpRequest) {
        return smsService.sendOTP(otpRequest.getMobile(), otpRequest.getISDCode());
    }

    @Operation(summary = "verify otp without user details", method = "POST")
    @PostMapping("/verify-otp")
    public ResponseEntity<VerifyOTPResponse> verifyOTP(@RequestBody VerifyOTPRequest otpRequest) {
        return smsService.verifyOTP(otpRequest);
    }
}
