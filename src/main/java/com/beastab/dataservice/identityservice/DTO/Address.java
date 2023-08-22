package com.beastab.dataservice.identityservice.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.beastab.dataservice.common.enums.Country;
import com.beastab.dataservice.identityservice.AddressType;
import com.beastab.dataservice.identityservice.db.entity.AddressEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
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
    private String gstNumber;
    @Field(name = "address_type")
    @JsonProperty("address_type")
    private String addressType;
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

    public Address(AddressEntity entity) {
        this.companyID = entity.getCompanyID();
        if (Objects.nonNull(entity.getAddressType()))
            this.addressType = entity.getAddressType().getDisplayName();
        else
            this.addressType = AddressType.OFFICE.getDisplayName();
        this.gstNumber = entity.getGSTNumber();
        this.addressLine1 = withNullCheck(entity.getAddressLine1());
        this.addressLine2 = withNullCheck(entity.getAddressLine2());
        this.addressLine3 = withNullCheck(entity.getAddressLine3());
        this.country = entity.getCountry();
        this.city = entity.getCity();
        this.state = entity.getState();
        this.pincode = entity.getPincode();
        if (CollectionUtils.isEmpty(entity.getMobileNumber())) {
            MobileNumber m = new MobileNumber("", "", false);
            List<MobileNumber> mList = new ArrayList<>();
            mList.add(m);
            this.mobileNumber = mList;
        } else {
            this.mobileNumber = entity.getMobileNumber();
        }
        this.fullName = entity.getFullName();
        this.addressId = entity.getAddressId();
        this.isDeleted = false;
        this.zohoAddressId = entity.getZohoAddressId();
        this.addedBy = entity.getAddedBy();

    }


    private String withNullCheck(String val) {
        if (StringUtils.isNotEmpty(val))
            return val;
        return "";
    }
}
