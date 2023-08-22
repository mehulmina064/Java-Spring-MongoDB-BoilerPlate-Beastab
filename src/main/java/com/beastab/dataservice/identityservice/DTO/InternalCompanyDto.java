package com.beastab.dataservice.identityservice.DTO;

import com.beastab.dataservice.identityservice.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.beastab.dataservice.identityservice.db.entity.ClientEntity;
import com.beastab.dataservice.identityservice.db.entity.CompanyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InternalCompanyDto {
    private String companyId;
    private String designation;
    private UserRole role;

    public InternalCompanyDto(CompanyEntity companyEntity, ClientEntity client) {
        this.companyId = companyEntity.getCompanyId();
        List<UserCompanyRole> companyRoleList = client.getCompanyRoles().parallelStream().filter(c -> Objects.equals(companyEntity.getCompanyId(), c.getCompanyID())).collect(Collectors.toList());
        this.designation = companyRoleList.get(0).getDesignation();
        this.role = companyRoleList.get(0).getRole();
    }
}
