package com.beastab.dataservice.identityservice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.beastab.dataservice.identityservice.enums.UserRole;
import lombok.Data;

@Data
public class ClientUpdateRequest {
    @JsonProperty(value = "isd", required = false)
    private String ISDCode;
    @JsonProperty(value = "mobile", required = false)
    private String mobile;
    @JsonProperty(value = "name", required = false)
    private String name;
    @JsonProperty(value = "email", required = false)
    private String email;
    @JsonProperty("role")
    private UserRole role;
    @JsonProperty("designation")
    private String designation;
    @JsonProperty("company_id")
    private String companyId;
}
