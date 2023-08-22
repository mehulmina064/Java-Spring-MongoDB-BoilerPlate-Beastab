package com.beastab.dataservice.identityservice.db.repository;


import com.beastab.dataservice.identityservice.db.entity.EmailerEntity;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class EmailerRepository {

    public static final String MAILER = "Mailer_Group";

    @Autowired
    private MongoTemplate mongoTemplate;

    public void bulkUpsert(List<EmailerEntity> emailerEntityList) {
        try {
            BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, EmailerEntity.class, MAILER);
            for (EmailerEntity emailer : emailerEntityList) {
                Document doc = new Document();
                mongoTemplate.getConverter().write(emailer, doc);
                Query query = new Query(Criteria.where("mail_group_id").is(doc.get("mail_group_id")));
                Document updateDoc = new Document();
                updateDoc.append("$set", doc);
                Update update = Update.fromDocument(updateDoc, "_id");
                bulkOps.upsert(query, update);
            }
            bulkOps.execute();
        }catch (Exception e){
            log.error("[EmailerRepository:bulkUpsert] Error While Adding data into Db. {} ",e.getMessage());
            e.printStackTrace();
        }
    }

    public EmailerEntity findByMailerGroupId(String mailGroupId, String clientId, String companyId) {
        EmailerEntity emailerEntity = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("mail_group_id").is(mailGroupId));
            query.addCriteria(Criteria.where("client_id").is(clientId));
            query.addCriteria(Criteria.where("company_id").is(companyId));
            query.addCriteria(Criteria.where("is_deleted").is(false));
            emailerEntity = mongoTemplate.findOne(query,
                    EmailerEntity.class, MAILER);

        } catch (Exception e) {
            log.error("[EmailerRepository:findByMailerGroupId] Error While fetching data from Db. {} ",e.getMessage());
            e.printStackTrace();
        }
        return emailerEntity;
    }

    public List<EmailerEntity> findAllData(String clientId, String companyId) {
        List<EmailerEntity> emailerEntity = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("client_id").is(clientId));
            query.addCriteria(Criteria.where("company_id").is(companyId));
            query.addCriteria(Criteria.where("is_deleted").is(false));
            emailerEntity = mongoTemplate.find(query,
                    EmailerEntity.class, MAILER);

        } catch (Exception e) {
            log.error("[EmailerRepository:findAllData] Error While fetching data from Db. {} ",e.getMessage());
            e.printStackTrace();
        }
        return emailerEntity;
    }

    public List<EmailerEntity> findByMultipleMailerIds(List<String> mailGroupIds) {
        List<EmailerEntity> emailerEntity = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("mail_group_id").in(mailGroupIds));
            query.addCriteria(Criteria.where("is_deleted").is(false));
            emailerEntity = mongoTemplate.find(query,
                    EmailerEntity.class, MAILER);

        } catch (Exception e) {
            log.error("[EmailerRepository:findByMultipleMailerIds] Error While fetching data from Db. {} ",e.getMessage());
            e.printStackTrace();
        }
        return emailerEntity;
    }
}
