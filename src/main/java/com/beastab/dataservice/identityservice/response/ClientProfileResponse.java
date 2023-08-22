package com.beastab.dataservice.identityservice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.beastab.dataservice.identityservice.DTO.UserCompanyRole;
import com.beastab.dataservice.identityservice.db.entity.ClientEntity;
import com.beastab.dataservice.identityservice.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientProfileResponse {
    private String name;
    private String email;
    private String mobile;
    private String isd;
    private UserRole role;
    private String designation;
    private String department;
    private String image;

    public ClientProfileResponse(ClientEntity clientEntity, String companyId){
        this.name = clientEntity.getName();
        this.email = clientEntity.getEmail();
        this.mobile = clientEntity.getMobile();
        this.isd = clientEntity.getISDCode();
        UserCompanyRole userCompanyRole = ClientEntity.getUserCompanyRole(clientEntity, companyId);
        this.role = userCompanyRole.getRole();
        this.designation = userCompanyRole.getDesignation();
        this.department = "";
        this.image = StringUtils.isEmpty(clientEntity.getProfileImage()) ? "" : clientEntity.getProfileImage();
    }
}
