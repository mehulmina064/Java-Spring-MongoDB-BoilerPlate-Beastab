package com.beastab.dataservice.identityservice.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientRegisterationRequest {

    @JsonProperty("isd_code")
    private String ISDCode;

    private String mobile;

    private String name;

    private String email;

    private String country;

    private String designation;

    private String company;

    private String password;

    private Boolean isVerifyFromEmail;

}
