package com.beastab.dataservice.identityservice.controller;

import com.beastab.dataservice.common.utils.CommonUtils;
import com.beastab.dataservice.identityservice.request.AddAddressRequest;
import com.beastab.dataservice.identityservice.request.UpdateAddressRequest;
import com.beastab.dataservice.identityservice.response.AllAddressResponse;
import com.beastab.dataservice.identityservice.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Tag(name = "Identity Service - Address related APIs")
@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Operation(summary = "Add new addresses", method = "POST")
    @PostMapping("/add")
    public ResponseEntity<String> addAddress(HttpServletRequest req, @RequestBody AddAddressRequest addressRequest) {
        HashMap<String, String> auth = CommonUtils.getClientAndCompany(req);
        return addressService.addAddress(auth, addressRequest);
    }
    @Operation(summary = "Get all addresses", method = "GET")
    @GetMapping("/all")
    public ResponseEntity<AllAddressResponse> getAllAddresses(HttpServletRequest req) {
        HashMap<String, String> auth = CommonUtils.getClientAndCompany(req);
        return addressService.getAllAddresses(auth, "") ;
    }
    @Operation(summary = "Get address by id", method = "GET")
    @GetMapping("")
    public ResponseEntity<AllAddressResponse> getAllAddresses(HttpServletRequest req,
                                                              @RequestParam(value = "address_id", required = true) String addressId) {
        HashMap<String, String> auth = CommonUtils.getClientAndCompany(req);

        return addressService.getAllAddresses(auth, addressId);
    }

    @Operation(summary = "Update address", method = "POST")
    @PostMapping("/update")
    public ResponseEntity<String> updateAddress(@RequestBody UpdateAddressRequest updateRequest) {
        return addressService.updateAddress(updateRequest);
    }

    @Operation(summary = "Update address", method = "POST")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAddress(@RequestParam(value = "address_id", required = true)
                                                    String addressId) {
        return addressService.deleteAddress(addressId);
    }

}
