package com.beastab.dataservice.identityservice.db.repository;

import com.beastab.dataservice.identityservice.db.entity.AddressEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class AddressRepository {

    public static final String ADDRESSES = "addresses";
    public static final String ADDRESS_ID = "address_id";
    @Autowired
    private MongoTemplate mongoTemplate;
    public void bulkUpsert(List<AddressEntity> addressEntities){
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, AddressEntity.class, ADDRESSES);
        for (AddressEntity address : addressEntities) {
            Document doc = new Document();
            mongoTemplate.getConverter().write(address, doc);
            Query query = new Query(Criteria.where(ADDRESS_ID).is(doc.get(ADDRESS_ID)));
            Document updateDoc = new Document();
            updateDoc.append("$set", doc);
            Update update = Update.fromDocument(updateDoc, "_id");
            bulkOps.upsert(query, update);
        }
        bulkOps.execute();
    }

    public List<AddressEntity> getAddressesByCompany(String companyId){
        List<AddressEntity> addresses = new LinkedList<>();
        try {
            Query query = new Query();
            if(StringUtils.isNoneEmpty(companyId)){
                query.addCriteria(Criteria.where("company_id").is(companyId));
            }
            query.addCriteria(Criteria.where("is_deleted").is(false));
            addresses = mongoTemplate.find(query,
                    AddressEntity.class, ADDRESSES);
            return addresses;
        } catch (Exception e) {
            log.error("AddressRepository.getAddresses", e);
            return addresses;
        }
    }

    public AddressEntity getAddressById(String addressId){
        AddressEntity addressEntity = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where(ADDRESS_ID).is(addressId));
            query.addCriteria(Criteria.where("is_deleted").is(false));
            addressEntity = mongoTemplate.findOne(query,
                    AddressEntity.class, ADDRESSES);
            return addressEntity;
        } catch (Exception e) {
            log.error("AddressRepository.getAddressById", e);
            return null;
        }
    }
    public Map<String, AddressEntity> getMultipleAddresses(
            List<String> productIds) {
        Map<String, AddressEntity> entityMap = new HashMap<>();
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where(ADDRESS_ID).in(productIds));
            List<AddressEntity> documents = mongoTemplate.find(query,
                    AddressEntity.class, ADDRESSES);
            for (AddressEntity document : documents) {
                String productID = document.getAddressId();
                entityMap.put(productID, document);
            }
            return entityMap;
        } catch (Exception e) {
            log.error("getMultipleAddresses", e);
            return entityMap;
        }
    }

    public void upsert(AddressEntity addressEntity) {
        Document doc = new Document();
        mongoTemplate.getConverter().write(addressEntity, doc);
        Query query = new Query(Criteria.where(ADDRESS_ID).is(doc.get(ADDRESS_ID)));
        Document updateDoc = new Document();
        updateDoc.append("$set", doc);
        Update update = Update.fromDocument(updateDoc, "_id");

        mongoTemplate.upsert(query, update, AddressEntity.class,ADDRESSES);
    }




}
