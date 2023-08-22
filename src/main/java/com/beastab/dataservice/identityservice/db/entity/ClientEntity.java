package com.beastab.dataservice.identityservice.db.entity;

import com.beastab.dataservice.identityservice.DTO.UserCompanyRole;
import com.beastab.dataservice.identityservice.request.AddTeamMemberRequest;
import com.beastab.dataservice.identityservice.request.ClientRegisterationRequest;
import com.beastab.dataservice.identityservice.request.ClientUpdateRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.beastab.dataservice.common.db.entity.AbstractEntity;
import com.beastab.dataservice.common.utils.CommonUtils;
import com.beastab.dataservice.common.utils.EncryptionUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Document(collection = "clients")
public class ClientEntity extends AbstractEntity {

    @Field(name = "client_id")
    private String clientId;

    @Field(name = "name")
    private String name;

    @Field(name = "email")
    private String email;

    @Field(name = "company_roles")
    private List<UserCompanyRole> companyRoles;

    @Field(name = "password")
    private String password;

    @Field(name = "mobile")
    private String mobile;

    @Field(name = "isd")
    private String ISDCode;

    @Field(name = "is_disabled")
    private boolean isDisabled;

    @Field(name = "profile_image")
    private String profileImage;

    @Field(name = "onesignal_token")
    private String onesignalToken;


    @Field(name = "is_email_verified")
    private boolean isEmailVerified;

    @Field(name = "otp_verify_time")
    private LocalDateTime otpVerifyTime;

    @Field(name = "email_verification_otp")
    private String emailVerificationOtp;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Field(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Field(name = "last_active_at")
    private LocalDateTime lastActiveAt;


    @Field(name = "source")
    private String source;

    public ClientEntity(ClientRegisterationRequest clientRegisterationRequest, String companyId, String source) {
        this.clientId = UUID.randomUUID().toString();
        this.name = clientRegisterationRequest.getName();
        this.password = EncryptionUtils.encryptPassword(clientRegisterationRequest.getPassword());
        this.email = clientRegisterationRequest.getEmail();
        this.mobile = clientRegisterationRequest.getMobile();
        this.companyRoles = createUserCompanyRole(clientRegisterationRequest, companyId);
        this.ISDCode = clientRegisterationRequest.getISDCode();
        this.setDisabled(false);
        this.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        this.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        this.setLastLoginAt(LocalDateTime.now(ZoneOffset.UTC));
        this.setLastActiveAt(LocalDateTime.now(ZoneOffset.UTC));
        this.source = source;
        //TODO Client Profile Image
    }

    public static void updatePassword(ClientEntity entity, String newPassword){
        entity.setPassword(EncryptionUtils.encryptPassword(newPassword));
        entity.setDisabled(false);
        entity.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
    }
    public static void updateClientEntity(ClientEntity clientEntity, ClientUpdateRequest clientUpdateRequest){
        //Update email
        if(!StringUtils.equalsIgnoreCase(clientEntity.getEmail(), clientUpdateRequest.getEmail()))
            clientEntity.setEmail(clientUpdateRequest.getEmail());

        //Update name
        if(!StringUtils.equalsIgnoreCase(clientEntity.getName(), clientUpdateRequest.getName()))
            clientEntity.setName(clientUpdateRequest.getName());

        //Update mobile
        if(!StringUtils.equalsIgnoreCase(clientEntity.getMobile(), clientUpdateRequest.getMobile()))
            clientEntity.setMobile(clientUpdateRequest.getMobile());

        //Update Company Roles
//        if(!ListUtils.isEqualList(clientEntity.getCompanyRoles(), clientUpdateRequest.getCompanyRoles()))
//            clientEntity.setCompanyRoles(clientUpdateRequest.getCompanyRoles());

        //Update isd
        if(!StringUtils.equalsIgnoreCase(clientEntity.getISDCode(), clientUpdateRequest.getISDCode()))
            clientEntity.setISDCode(clientUpdateRequest.getISDCode());

        if(ObjectUtils.isNotEmpty(clientEntity.getCompanyRoles())){

            for(UserCompanyRole companyRole : clientEntity.getCompanyRoles()){

                if(Objects.equals(companyRole.getCompanyID(), clientUpdateRequest.getCompanyId())){

                    if(Objects.nonNull(clientUpdateRequest.getRole())) companyRole.setRole(clientUpdateRequest.getRole());
                    if(Objects.nonNull(clientUpdateRequest.getDesignation())) companyRole.setDesignation(clientUpdateRequest.getDesignation());
                }
            }
        }
    }
    public ClientEntity(AddTeamMemberRequest addTeamMemberRequest, String companyId) {
        this.clientId = UUID.randomUUID().toString();
        this.name = addTeamMemberRequest.getName();
        this.password = EncryptionUtils.encryptPassword(CommonUtils.DEFAULT_PASSWORD);
        this.email = addTeamMemberRequest.getEmail();
        this.mobile = addTeamMemberRequest.getMobile();
        this.companyRoles = createUserCompanyRole(addTeamMemberRequest, companyId);
        this.ISDCode = addTeamMemberRequest.getIsd();
        this.setDisabled(false);
        this.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        this.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        this.setLastLoginAt(LocalDateTime.now(ZoneOffset.UTC));
        this.setLastActiveAt(LocalDateTime.now(ZoneOffset.UTC));
        this.profileImage = addTeamMemberRequest.getProfileImage();
    }


    private List<UserCompanyRole> createUserCompanyRole(ClientRegisterationRequest clientRegisterationRequest, String companyId){
        UserCompanyRole userCompanyRole = new UserCompanyRole(clientRegisterationRequest, companyId, true);
        List<UserCompanyRole> userCompanyRoles = new ArrayList<>();
        userCompanyRoles.add(userCompanyRole);
        return userCompanyRoles;
    }


    private List<UserCompanyRole> createUserCompanyRole(AddTeamMemberRequest addTeamMemberRequest, String companyId){
        UserCompanyRole userCompanyRole = new UserCompanyRole(addTeamMemberRequest, companyId);
        List<UserCompanyRole> userCompanyRoles = new ArrayList<>();
        userCompanyRoles.add(userCompanyRole);
        return userCompanyRoles;
    }



    public static UserCompanyRole getPrimaryUserCompanyRole(ClientEntity entity){
        for(UserCompanyRole userCompanyRole: entity.getCompanyRoles()){
            if(userCompanyRole.isPrimary())
                return userCompanyRole;
        }
        return null;
    }

    public static UserCompanyRole getUserCompanyRole(ClientEntity entity, String companyId){
        for(UserCompanyRole userCompanyRole: entity.getCompanyRoles()){
            if(StringUtils.equalsIgnoreCase(userCompanyRole.getCompanyID(), companyId))
                return userCompanyRole;
        }
        return null;
    }



}
