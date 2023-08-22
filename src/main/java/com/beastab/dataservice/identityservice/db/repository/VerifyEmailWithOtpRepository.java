package com.beastab.dataservice.identityservice.db.repository;


import com.beastab.dataservice.identityservice.db.entity.VerifyEmailWithOtpEntity;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Slf4j
public class VerifyEmailWithOtpRepository {

    @Autowired
    private MongoTemplate mongoTemplate;


    public void saveOtpForVerification(String emailId, String otp) {
        VerifyEmailWithOtpEntity verifyEmailWithOtpEntity = VerifyEmailWithOtpEntity.builder()
                .email(emailId)
                .otp(otp)
                .createdAt(LocalDateTime.now())
                .build();
        mongoTemplate.save(verifyEmailWithOtpEntity);
    }

    public VerifyEmailWithOtpEntity findByEmailId(String emailId) {
        VerifyEmailWithOtpEntity verifyEmailWithOtpEntity = new VerifyEmailWithOtpEntity();
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("email").is(emailId));
            verifyEmailWithOtpEntity = mongoTemplate.findOne(query,
                    VerifyEmailWithOtpEntity.class, "email_verification");

        } catch (Exception e) {
            log.error("[VerifyEmailWithOtpRepository:findByEmailId] Error While getting fetch data from db . {}", e.getMessage());
            e.printStackTrace();
        }
        return verifyEmailWithOtpEntity;
    }

    public void bulkUpsert(List<VerifyEmailWithOtpEntity> verifyEmailWithOtpEntities) {
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, VerifyEmailWithOtpEntity.class, "email_verification");
        for (VerifyEmailWithOtpEntity entity : verifyEmailWithOtpEntities) {
            Document doc = new Document();
            mongoTemplate.getConverter().write(entity, doc);
            Query query = new Query(Criteria.where("email").is(doc.get("email")));
            Document updateDoc = new Document();
            updateDoc.append("$set", doc);
            Update update = Update.fromDocument(updateDoc, "_id");
            bulkOps.upsert(query, update);
        }
        bulkOps.execute();
    }
}
