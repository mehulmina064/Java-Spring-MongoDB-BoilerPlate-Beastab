package com.beastab.dataservice.communicationservice.controller;

import com.beastab.dataservice.identityservice.db.entity.EmailerEntity;
import com.beastab.dataservice.identityservice.db.repository.EmailerRepository;
import com.beastab.dataservice.identityservice.exceptions.FetchTeamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
public class TestController {


    @Autowired
    private EmailerRepository emailerRepository;

//    @Autowired
//    private SampleService sampleService;


    @GetMapping("/")
    public void test() {
        throw new FetchTeamException("Tetsing");
    }

//    @GetMapping("/test1")
//    public void test2() throws MessagingException {
//        SampleEntity sampleEntity = new SampleEntity();
//        sampleEntity.setClientId("9fbc84c0-f08c-4f5f-b99e-df3a8601cb6f");
//        sampleEntity.setCompanyId("7fb713f5-6a78-4e47-8562-5ae8f66cf7a0");
//        sampleEntity.setProductId("645e11fbd222f38a8a2e5290");
//        sampleService.sendMailToSalesTeam(sampleEntity);
//    }

}
