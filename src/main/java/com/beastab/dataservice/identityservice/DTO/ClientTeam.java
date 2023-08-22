package com.beastab.dataservice.identityservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.beastab.dataservice.common.utils.CommonUtils;
import com.beastab.dataservice.identityservice.db.entity.ClientEntity;
import com.beastab.dataservice.identityservice.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientTeam {
    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_name")
    private String clientName;

    @JsonProperty("role")
    private UserRole role;

    @JsonProperty("designation")
    private String designation;

    @JsonProperty("image")
    private String image;

    @JsonProperty("email")
    private String email;

    public ClientTeam(ClientEntity clientEntity, String companyId){
        this.clientId = clientEntity.getClientId();
        this.clientName = clientEntity.getName();
        UserCompanyRole userCompanyRole = ClientEntity.getUserCompanyRole(clientEntity, companyId);
        this.role = userCompanyRole.getRole();
        this.designation = userCompanyRole.getDesignation();
        this.image = clientEntity.getProfileImage();
        if(StringUtils.isEmpty(this.image))
            this.image = CommonUtils.DEFAULT_AVATAR;
        this.email = clientEntity.getEmail();
    }

}
