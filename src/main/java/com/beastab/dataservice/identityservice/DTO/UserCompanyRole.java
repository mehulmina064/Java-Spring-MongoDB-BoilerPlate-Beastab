package com.beastab.dataservice.identityservice.DTO;

import com.beastab.dataservice.identityservice.enums.UserRole;
import com.beastab.dataservice.identityservice.request.AddTeamMemberRequest;
import com.beastab.dataservice.identityservice.request.ClientRegisterationRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.beastab.dataservice.common.enums.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserCompanyRole {

    @Field(name = "company_id")
    private String companyID;

    @Field(name = "role")
    private UserRole role;

    @Field(name = "designation")
    private String designation;

    @Field(name = "country")
    private Country country;

    @Field(name = "is_primary")
    private boolean isPrimary;

    public UserCompanyRole(ClientRegisterationRequest clientRegisterationRequest, String companyID, boolean isPrimary){
        this.companyID = companyID;
        this.role = UserRole.USER;
        this.designation = clientRegisterationRequest.getDesignation();
        this.country = Country.getCountryByName(clientRegisterationRequest.getCountry());
        this.isPrimary = isPrimary;

    }

    public UserCompanyRole(AddTeamMemberRequest addTeamMemberRequest, String companyID){
        this.companyID = companyID;
        this.role = addTeamMemberRequest.getRole();
        this.designation = addTeamMemberRequest.getDesignation();
        this.isPrimary = false;
    }


    public UserCompanyRole(InternalCompanyDto company){
        this.companyID = company.getCompanyId();
        this.designation = company.getDesignation();
        this.role = company.getRole();
        this.isPrimary = false;
        this.country = Country.INDIA;
    }

}
