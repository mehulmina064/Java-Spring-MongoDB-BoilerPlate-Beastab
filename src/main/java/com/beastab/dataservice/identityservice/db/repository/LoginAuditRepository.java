package com.beastab.dataservice.identityservice.db.repository;

import com.beastab.dataservice.identityservice.db.entity.LoginAuditEntity;
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
public class LoginAuditRepository {

    @Autowired
    private MongoTemplate mongoTemplate;
    public void bulkUpsert(List<LoginAuditEntity> loginAuditEntities) {
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, LoginAuditEntity.class, "login_audit");
        for (LoginAuditEntity loginAuditEntity : loginAuditEntities) {
            Document doc = new Document();
            mongoTemplate.getConverter().write(loginAuditEntity, doc);
            Query query = new Query(Criteria.where("login_audit_id").is(doc.get("login_audit_id")));
            Document updateDoc = new Document();
            updateDoc.append("$set", doc);
            Update update = Update.fromDocument(updateDoc, "_id");
            bulkOps.upsert(query, update);
        }
        bulkOps.execute();
    }
}
