package com.beastab.dataservice.identityservice.db.repository;

import com.beastab.dataservice.identityservice.db.entity.CountryEntity;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class CountryRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<CountryEntity> findEnabledCountries(){
        List<CountryEntity> countryList = new ArrayList<>();
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("is_disabled").is(false));
            countryList = mongoTemplate.find(query,
                    CountryEntity.class, "countries");
            return countryList;
        } catch (Exception e) {
            log.error("CompanyRepository.findCompanyByName", e);
            return countryList;
        }
    }

    public void bulkUpsert(List<CountryEntity> countryEntities){
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, CountryEntity.class, "countries");
        for (CountryEntity client : countryEntities) {
            Document doc = new Document();
            mongoTemplate.getConverter().write(client, doc);
            Query query = new Query(Criteria.where("country").is(doc.get("country")));
            Document updateDoc = new Document();
            updateDoc.append("$set", doc);
            Update update = Update.fromDocument(updateDoc, "_id");
            bulkOps.upsert(query, update);
        }
        bulkOps.execute();
    }
}
