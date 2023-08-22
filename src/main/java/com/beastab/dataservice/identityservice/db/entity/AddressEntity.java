package com.beastab.dataservice.identityservice.db.entity;

import com.beastab.dataservice.identityservice.DTO.MobileNumber;
import com.beastab.dataservice.identityservice.request.AddAddressRequest;
import com.beastab.dataservice.identityservice.request.UpdateAddressRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.beastab.dataservice.common.db.entity.AbstractEntity;
import com.beastab.dataservice.common.enums.Country;
import com.beastab.dataservice.identityservice.AddressType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Document(collection = "addresses")
public class AddressEntity extends AbstractEntity {

    @Field(name = "address_id")
    @JsonProperty("address_id")
    private String addressId;
    @Field(name = "full_name")
    @JsonProperty("full_name")
    private String fullName;
    @Field(name = "mobiles")
    @JsonProperty("mobiles")
    private List<MobileNumber> mobileNumber;
    @Field(name = "pincode")
    @JsonProperty("pincode")
    private String pincode;
    @Field(name = "city")
    @JsonProperty("city")
    private String city;
    @Field(name = "state")
    @JsonProperty("state")
    private String state;
    @Field(name = "country")
    @JsonProperty("country")
    private Country country;
    @Field(name = "address_line_1")
    @JsonProperty("address_line_1")
    private String addressLine1;
    @Field(name = "address_line_2")
    @JsonProperty("address_line_2")
    private String addressLine2;
    @Field(name = "address_line_3")
    @JsonProperty("address_line_3")
    private String addressLine3;
    @Field(name = "gst_number")
    @JsonProperty("gst_number")
    private String GSTNumber;
    @Field(name = "address_type")
    @JsonProperty("address_type")
    private AddressType addressType;
    @Field(name = "client_id")
    @JsonProperty("client_id")
    private String addedBy;
    @Field(name = "company_id")
    @JsonProperty("company_id")
    private String companyID;
    @Field(name = "is_deleted")
    @JsonProperty("is_deleted")
    private boolean isDeleted;
    @Field(name = "zoho_address_id")
    @JsonProperty("zoho_address_id")
    private String zohoAddressId;

    public AddressEntity(HashMap<String, String> auth, AddAddressRequest addressRequest) {
        this.addedBy = auth.getOrDefault("client_id", "");
        this.companyID = auth.getOrDefault("company_id", "");
        this.addressType = addressRequest.getAddressType();
        this.GSTNumber = addressRequest.getGSTNumber();
        this.addressLine1 = addressRequest.getAddressLine1();
        this.addressLine2 = addressRequest.getAddressLine2();
        this.addressLine3 = addressRequest.getAddressLine3();
        this.country = addressRequest.getCountry();
        this.city = addressRequest.getCity();
        this.state = addressRequest.getState();
        this.pincode = addressRequest.getPincode();
        this.mobileNumber = addressRequest.getMobileNumber();
        this.fullName = addressRequest.getFullName();
        if (addressRequest.getAddressId() == null) {
            this.addressId = UUID.randomUUID().toString();
        } else {
            this.addressId = addressRequest.getAddressId();
        }
        this.zohoAddressId=null;
        this.isDeleted = false;
    }

    public AddressEntity(UpdateAddressRequest updateAddressRequest){
        this.addressType = updateAddressRequest.getAddressType();
        this.GSTNumber = updateAddressRequest.getGSTNumber();
        this.addressLine1 = updateAddressRequest.getAddressLine1();
        this.addressLine2 = updateAddressRequest.getAddressLine2();
        this.addressLine3 = updateAddressRequest.getAddressLine3();
        this.country = updateAddressRequest.getCountry();
        this.city = updateAddressRequest.getCity();
        this.state = updateAddressRequest.getState();
        this.pincode = updateAddressRequest.getPincode();
        this.mobileNumber = updateAddressRequest.getMobileNumber();
        this.fullName = updateAddressRequest.getFullName();
        this.addressId = updateAddressRequest.getAddressId();
        this.isDeleted = false;
        this.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
    }
}
