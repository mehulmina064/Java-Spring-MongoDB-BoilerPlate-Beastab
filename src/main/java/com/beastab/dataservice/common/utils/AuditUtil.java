package com.beastab.dataservice.common.utils;

import com.beastab.dataservice.identityservice.db.entity.LoginAuditEntity;
import com.beastab.dataservice.identityservice.db.repository.LoginAuditRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AuditUtil {

    @Autowired
    private LoginAuditRepository loginAuditRepository;

    @Async
    public void audit(String source, String loginType, String email,
                      String password, String mobile, String clientId, String reason,
                      boolean isSuccessful) {
        try {
            LoginAuditEntity loginAuditEntity = new LoginAuditEntity(
                    source, loginType, email, password, mobile, clientId, reason, isSuccessful);
            List<LoginAuditEntity> entities = new ArrayList<>();
            entities.add(loginAuditEntity);
            loginAuditRepository.bulkUpsert(entities);
        } catch (Exception e){
            log.error("Issue with audit", e);
        }
    }
}
