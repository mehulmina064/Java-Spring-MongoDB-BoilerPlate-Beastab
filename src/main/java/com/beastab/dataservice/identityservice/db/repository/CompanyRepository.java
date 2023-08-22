package com.beastab.dataservice.identityservice.db.repository;

import com.beastab.dataservice.identityservice.db.entity.CompanyEntity;
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
public class CompanyRepository {
    @Autowired
    private MongoTemplate mongoTemplate;
    public CompanyEntity findCompanyByName(String companyName){
        CompanyEntity companyEntity = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("company_name").is(companyName));
            companyEntity = mongoTemplate.findOne(query,
                    CompanyEntity.class, "companies");
            return companyEntity;
        } catch (Exception e) {
            log.error("CompanyRepository.findCompanyByName", e);
            return companyEntity;
        }
    }
    public CompanyEntity findCompanyById(String companyId){
        CompanyEntity companyEntity = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("company_id").is(companyId));
            companyEntity = mongoTemplate.findOne(query,
                    CompanyEntity.class, "companies");
            return companyEntity;
        } catch (Exception e) {
            log.error("CompanyRepository.findCompanyById", e);
            return companyEntity;
        }
    }

    public CompanyEntity findCompanyByZohoId(String companyId){
        CompanyEntity companyEntity = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("zoho_id").is(companyId));
            companyEntity = mongoTemplate.findOne(query,
                    CompanyEntity.class, "companies");
            return companyEntity;
        } catch (Exception e) {
            log.error("CompanyRepository.findCompanyById", e);
            return companyEntity;
        }
    }

    public boolean addCompanies(List<CompanyEntity> companyEntities){
        try {
            BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, CompanyEntity.class, "companies");
            for (CompanyEntity company : companyEntities) {
                Document doc = new Document();
                mongoTemplate.getConverter().write(company, doc);
                Query query = new Query(Criteria.where("company_name").is(doc.get("company_name")));
                Document updateDoc = new Document();
                updateDoc.append("$set", doc);
                Update update = Update.fromDocument(updateDoc, "_id");
                bulkOps.upsert(query, update);
            }
            bulkOps.execute();
            return true;
        } catch (Exception e) {
            log.error("CompanyRepository.addCompanies", e);
            return false;
        }
    }


}
