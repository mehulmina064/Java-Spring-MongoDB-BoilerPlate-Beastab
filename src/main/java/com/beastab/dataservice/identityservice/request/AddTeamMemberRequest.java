package com.beastab.dataservice.identityservice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.beastab.dataservice.identityservice.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTeamMemberRequest {
    private String name;
    private String email;
    private String mobile;
    private String isd;
    private UserRole role;
    private String designation;
    private String department;
    @JsonProperty("profile_image")
    private String profileImage;

}
