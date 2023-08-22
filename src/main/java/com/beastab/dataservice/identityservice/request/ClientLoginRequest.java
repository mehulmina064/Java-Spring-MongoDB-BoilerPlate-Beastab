package com.beastab.dataservice.identityservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientLoginRequest {
    @Email
    private String email;
    private String password;
}
