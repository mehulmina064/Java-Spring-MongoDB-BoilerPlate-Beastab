package com.beastab.dataservice.identityservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.beastab.dataservice.common.utils.CommonUtils;
import com.beastab.dataservice.identityservice.db.entity.ClientEntity;
import com.beastab.dataservice.identityservice.db.entity.CompanyEntity;
import com.beastab.dataservice.identityservice.enums.UserRole;
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
public class Company {
    private String companyId;
    private String companyName;
    private String designation;
    private UserRole role;
    private String createdAt;

    public Company(CompanyEntity companyEntity) {
        this.companyId = companyEntity.getCompanyId();
        this.companyName = companyEntity.getName();
        this.createdAt= CommonUtils.convertToDisplayDate(companyEntity.getCreatedAt());
    }

    public Company(String companyId, String companyName) {
        this.companyId = companyId;
        this.companyName = companyName;
    }
    public Company(CompanyEntity companyEntity, ClientEntity client) {
        this.companyId = companyEntity.getCompanyId();
        this.companyName = companyEntity.getName();
        List<UserCompanyRole> companyRoleList = client.getCompanyRoles().parallelStream().filter(c -> Objects.equals(companyEntity.getCompanyId(), c.getCompanyID())).collect(Collectors.toList());
        this.designation = companyRoleList.get(0).getDesignation();
        this.role = companyRoleList.get(0).getRole();
    }

}
