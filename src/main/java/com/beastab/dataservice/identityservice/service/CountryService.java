package com.beastab.dataservice.identityservice.service;

import com.beastab.dataservice.identityservice.response.EnabledCountryResponse;
import com.beastab.dataservice.common.enums.Country;
import com.beastab.dataservice.identityservice.DTO.EnabledCountry;
import com.beastab.dataservice.identityservice.db.entity.CountryEntity;
import com.beastab.dataservice.identityservice.db.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;
    @Cacheable(cacheNames = "CountryCache")
    public ResponseEntity<EnabledCountryResponse> getEnabledCountries(){
        List<CountryEntity> countryEntities = countryRepository.findEnabledCountries();
        List<EnabledCountry> enabledCountries =
                countryEntities.parallelStream().map(c-> new EnabledCountry(c)).collect(Collectors.toList());
        return new ResponseEntity<EnabledCountryResponse>(new EnabledCountryResponse(enabledCountries), HttpStatus.OK);

    }

    public ResponseEntity<String> enableCountry(String countryName, String flagIcon){
        Country country = Country.getCountryByName(countryName);
        CountryEntity countryEntity = new CountryEntity(UUID.randomUUID().toString(), country, false, flagIcon);
        List<CountryEntity> countryEntities = new ArrayList<>();
        countryEntities.add(countryEntity);
        countryRepository.bulkUpsert(countryEntities);
        return new ResponseEntity<>("Country enabled successfully", HttpStatus.OK);
    }
}
