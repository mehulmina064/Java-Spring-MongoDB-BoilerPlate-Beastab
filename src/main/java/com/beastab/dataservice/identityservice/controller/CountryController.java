package com.beastab.dataservice.identityservice.controller;

import com.beastab.dataservice.identityservice.response.EnabledCountryResponse;
import com.beastab.dataservice.identityservice.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Identity Service - Country related APIs")
@RestController
@RequestMapping("/api/v1/country")
public class CountryController {
    @Autowired
    private CountryService countryService;
    @Operation(summary = "Get all enabled countries, isd code, flag icon", method = "GET")
    @GetMapping("")
    public ResponseEntity<EnabledCountryResponse> getEnabledCountries() {
        return countryService.getEnabledCountries();
    }

    @Operation(summary = "Enable/Add country", method = "POST")
    @PostMapping("")
    public ResponseEntity<String> enableCountry(
            @RequestParam(value = "country_name", required = true) String countryName,
            @RequestParam(value = "flag_icon", required = true) String flagIcon) {
        return countryService.enableCountry(countryName, flagIcon);
    }
}
