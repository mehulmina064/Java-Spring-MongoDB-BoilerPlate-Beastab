package com.beastab.dataservice.identityservice.service;

import com.beastab.dataservice.identityservice.response.AllAddressResponse;
import com.beastab.dataservice.identityservice.DTO.Address;
import com.beastab.dataservice.identityservice.db.entity.AddressEntity;
import com.beastab.dataservice.identityservice.db.repository.AddressRepository;
import com.beastab.dataservice.identityservice.request.AddAddressRequest;
import com.beastab.dataservice.identityservice.request.UpdateAddressRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;
    public ResponseEntity<String> addAddress(HashMap<String, String> auth, AddAddressRequest addressRequest){
        AddressEntity addressEntity = new AddressEntity(auth, addressRequest);
        List<AddressEntity> addressEntityList = new ArrayList<>();
//        addressEntityList.add(addressEntity);
//        addressRepository.bulkUpsert(addressEntityList);
        addressRepository.upsert(addressEntity);
        return new ResponseEntity<>("Address added successfully", HttpStatus.OK);
    }

    public ResponseEntity<AllAddressResponse> getAllAddresses(HashMap<String, String> authMap, String addressId){
        String companyId = authMap.getOrDefault("company_id", "");
        if(StringUtils.isNoneEmpty(companyId)) {
            List<Address> addresses = new ArrayList<>();
            List<AddressEntity> addressEntities = addressRepository.getAddressesByCompany(companyId);
            if(!CollectionUtils.isEmpty(addressEntities)){
                addresses = addressEntities.parallelStream().map(a-> new Address(a)).collect(Collectors.toList());
            }
            AllAddressResponse allAddressResponse = new AllAddressResponse(addresses);
            return new ResponseEntity<AllAddressResponse> (allAddressResponse, HttpStatus.OK);
        }
        return new ResponseEntity<AllAddressResponse> (new AllAddressResponse(), HttpStatus.OK);
    }

    public ResponseEntity<String> updateAddress(UpdateAddressRequest updateRequest){
        AddressEntity addressEntity = addressRepository.getAddressById(updateRequest.getAddressId());
        if(Objects.nonNull(addressEntity)){
            AddressEntity address = new AddressEntity(updateRequest);
            addressRepository.upsert(address);
            return new ResponseEntity<>("Address updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Address not found", HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<String> deleteAddress(String addressId){
        AddressEntity addressEntity = addressRepository.getAddressById(addressId);
        if(Objects.nonNull(addressEntity)){
            List<AddressEntity> addressEntityList = new ArrayList<>();
            addressEntity.setDeleted(true);
            addressEntity.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
            addressEntityList.add(addressEntity);
            addressRepository.bulkUpsert(addressEntityList);
            return new ResponseEntity<>("Address deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Address not found", HttpStatus.FORBIDDEN);

    }
}
