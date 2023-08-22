package com.beastab.dataservice.identityservice.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.beastab.dataservice.common.utils.CommonUtils;
import com.beastab.dataservice.identityservice.db.entity.ClientEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    private String id;
    private String email;
    private String mobile;
    private String name;
    private String role;
    @JsonProperty("profile_image")
    private String profileImage;
    @JsonProperty("current_company_id")
    private String currentCompanyId;
    @JsonProperty("current_company_name")
    private String currentCompanyName;
    @JsonProperty("designation")
    private String designation;

    public Client(ClientEntity clientEntity, String currentCompanyName) {
        this.id = clientEntity.getClientId();
        this.email = clientEntity.getEmail();
        this.mobile = clientEntity.getMobile();
        this.name = clientEntity.getName();
        UserCompanyRole userCompanyRole = ClientEntity.getPrimaryUserCompanyRole(clientEntity);
        this.role = userCompanyRole.getRole().toString();
        this.currentCompanyId = userCompanyRole.getCompanyID();
        if(StringUtils.isNoneEmpty(clientEntity.getProfileImage()))
            this.profileImage = clientEntity.getProfileImage();
        else
            this.profileImage = CommonUtils.DEFAULT_AVATAR;
        this.designation = userCompanyRole.getDesignation();
        this.currentCompanyName = currentCompanyName;
    }

    public Client(ClientEntity clientEntity, String companyId, String currentCompanyName) {
        this.id = clientEntity.getClientId();
        this.email = clientEntity.getEmail();
        this.mobile = clientEntity.getMobile();
        this.name = clientEntity.getName();
        UserCompanyRole userCompanyRole = ClientEntity.getUserCompanyRole(clientEntity, companyId);
        this.role = userCompanyRole.getRole().toString();
        this.currentCompanyId = userCompanyRole.getCompanyID();
        if(StringUtils.isNoneEmpty(clientEntity.getProfileImage()))
            this.profileImage = clientEntity.getProfileImage();
        else
            this.profileImage = "";
        this.designation = userCompanyRole.getDesignation();
        this.currentCompanyName = currentCompanyName;
    }

    public Client(ClientEntity clientEntity) {
        this.id = clientEntity.getClientId();
        this.email = clientEntity.getEmail();
        this.name = clientEntity.getName();
        this.mobile = clientEntity.getMobile();
        this.profileImage = clientEntity.getProfileImage();
        for (UserCompanyRole companyRole: clientEntity.getCompanyRoles()) {
           if(StringUtils.isNotEmpty(companyRole.getCompanyID()) && companyRole.isPrimary()){
                this.currentCompanyId = companyRole.getCompanyID();
                this.designation = companyRole.getDesignation();
            }
        }


    }
}
