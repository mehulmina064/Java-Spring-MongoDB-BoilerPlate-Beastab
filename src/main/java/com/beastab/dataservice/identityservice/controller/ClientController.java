package com.beastab.dataservice.identityservice.controller;

import com.beastab.dataservice.identityservice.request.*;
import com.beastab.dataservice.identityservice.response.*;
import com.beastab.dataservice.common.utils.CommonUtils;
import com.beastab.dataservice.communicationservice.request.OTPRequest;
import com.beastab.dataservice.identityservice.enums.UserRole;
import com.beastab.dataservice.identityservice.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Tag(name = "Identity Service - User related APIs")
@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    @Autowired
    private ClientService clientService;


    @Operation(summary = "Login with username password", method = "POST")
    @PostMapping("/login")
    public ResponseEntity<ClientLoginResponse> login(@RequestBody ClientLoginRequest loginRequest,
                                                     @RequestParam(value = "source", required = false) String source) {
        return clientService.login(loginRequest, StringUtils.isNotEmpty(source)? source : "WEB");
    }

    @Operation(summary = "New user registration", method = "POST")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody ClientRegisterationRequest clientRegisterationRequest,
                                           @RequestParam(value = "source", required = false)
                                           String source) {
        return clientService.register(clientRegisterationRequest, StringUtils.isNotEmpty(source)? source : "CLIENT_APP");
    }

    @Operation(summary = "Send OTP for Forgot Password", method = "POST")
    @PostMapping("/send-otp")
    public ResponseEntity<ClientSendOTPResponse> sendOTPWithClientCheck(@RequestBody OTPRequest otpRequest,
                                                                        @RequestParam(value = "source", required = false) String source) {
        return clientService.sendOTPWithClientCheck(otpRequest);
    }

    @Operation(summary = "Reset Password - Update password with OTP verify", method = "POST")
    @PostMapping("/reset-password")
    public ResponseEntity<ClientLoginResponse> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest,
                                                             @RequestParam(value = "source", required = false) String source) {
        return clientService.resetPassword(resetPasswordRequest);
    }

    @Operation(summary = "Reset password with verify OTP flow", method = "POST")
    @PostMapping("/login-otp")
    public ResponseEntity<ClientLoginResponse> verifyOTP(@RequestBody ClientOTPLoginRequest resetPasswordRequest,
                                                         @RequestParam(value = "source", required = false) String source) {
        return clientService.verifyOTP(resetPasswordRequest, StringUtils.isNotEmpty(source)? source : "WEB");
    }

    @Operation(summary = "Update profile image", method = "POST")
    @PostMapping("/update-image")
    public ResponseEntity<String> updateImage(
            @RequestParam("file") MultipartFile file, HttpServletRequest req) {
        HashMap<String, String> auth = CommonUtils.getClientAndCompany(req);
        return clientService.updateImage(file, auth);
    }

    //edit client detail
    @Operation(summary = "Edit details of a client", method = "PATCH")
    @PatchMapping("/edit-client/{client_id}")
    public String editClientById(
            @PathVariable("client_id") String clientId,
            @RequestBody ClientUpdateRequest clientUpdateRequest
    ) {
        return clientService.editClient(clientId, clientUpdateRequest);
    }


    //edit client detail For Web
    @Operation(summary = "Edit details of a client for web", method = "PATCH")
    @PatchMapping("/edit-client-web/{client_id}")
    public ResponseEntity<ClientLoginResponse> editClientByIdForWeb(
            @PathVariable("client_id") String clientId,
            @RequestBody ClientUpdateRequest clientUpdateRequest
    ) {
        return clientService.editClientForWeb(clientId, clientUpdateRequest);
    }


    //make admin
    @Operation(summary = "Promote User to Admin", method = "POST")
    @PostMapping("/team/promote-to-admin")
    public ResponseEntity<String> promoteToAdmin(HttpServletRequest req,
                                                 @RequestParam(value = "client_id") String clientId) {
        HashMap<String, String> auth = CommonUtils.getClientAndCompany(req);
        return clientService.updateTeamMember(auth, clientId, UserRole.ADMIN);
    }

    @Operation(summary = "Demote User from Admin", method = "POST")
    @PostMapping("/team/demote-admin")
    public ResponseEntity<String> demoteAdmin(HttpServletRequest req,
                                              @RequestParam(value = "client_id") String clientId) {
        HashMap<String, String> auth = CommonUtils.getClientAndCompany(req);
        return clientService.updateTeamMember(auth, clientId, UserRole.USER);
    }

    @Operation(summary = "Get my team", method = "GET")
    @GetMapping("/team")
    public ResponseEntity<ClientTeamResponse> getMyTeam(HttpServletRequest req) {
        HashMap<String, String> auth = CommonUtils.getClientAndCompany(req);
        return clientService.getMyTeam(auth);
    }

    @Operation(summary = "Add team member", method = "POST")
    @PostMapping("/team/add")
    public ResponseEntity<ClientTeamResponse> addTeamMember(HttpServletRequest req,
                                                            @RequestBody AddTeamMemberRequest addTeamMemberRequest) {
        HashMap<String, String> auth = CommonUtils.getClientAndCompany(req);
        return clientService.addTeamMember(auth, addTeamMemberRequest);
    }

    @Operation(summary = "Switch Company", method = "POST")
    @PostMapping("/switch-company")
    public ResponseEntity<ClientLoginResponse> switchCompany(HttpServletRequest req,
                                                             @RequestBody String companyId,
                                                             @RequestParam(value = "source", required = false) String source) {
        HashMap<String, String> auth = CommonUtils.getClientAndCompany(req);
        return clientService.switchCompany(auth, companyId);
    }

    @Operation(summary = "Get Company", method = "GET")
    @GetMapping("/companies")
    public ResponseEntity<?> getCompanies(HttpServletRequest req) {
        HashMap<String, String> auth = CommonUtils.getClientAndCompany(req);
        return clientService.getCompanies(auth);
    }


    @Operation(summary = "Get profile by client id", method = "GET")
    @GetMapping("/profile/{client-id}")
    public ResponseEntity<ClientProfileResponse> getProfile(HttpServletRequest req,
                                                            @PathVariable("client-id") String clientId
    ) {
        HashMap<String, String> auth = CommonUtils.getClientAndCompany(req);
        return clientService.getProfile(clientId, auth);
    }

    @Operation(summary = "Soft delete user", method = "DELETE")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteClient(HttpServletRequest req) {
        HashMap<String, String> auth = CommonUtils.getClientAndCompany(req);
        return clientService.deleteProfile(auth);
    }

    @Operation(summary = "Soft delete user by admin", method = "DELETE")
    @DeleteMapping("/delete-user/{client-id}")
    public ResponseEntity<String> deleteUser(HttpServletRequest req,
                                             @PathVariable("client-id") String clientId) {
        HashMap<String, String> auth = CommonUtils.getClientAndCompany(req);
        String role = auth.getOrDefault("role", "");
        if (StringUtils.equalsIgnoreCase("ADMIN", role))
            return clientService.deleteProfile(clientId);
        else
            return new ResponseEntity<String>("Not allowed", HttpStatus.FORBIDDEN);
    }

    //    Get All Client
    @Operation(summary = "Get All Client", method = "GET")
    @GetMapping("clients")
    public ResponseEntity<ClientResponse> getAllClient(HttpServletRequest req) {
        HashMap<String, String> auth = CommonUtils.getClientAndCompany(req);
        return clientService.getAllClient(auth);

    }

}

