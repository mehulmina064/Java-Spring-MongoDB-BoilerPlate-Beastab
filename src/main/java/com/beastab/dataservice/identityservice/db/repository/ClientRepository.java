package com.beastab.dataservice.identityservice.db.repository;

import com.beastab.dataservice.identityservice.DTO.UserCompanyRole;
import com.beastab.dataservice.identityservice.db.entity.ClientEntity;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class ClientRepository {
    public static final String CLIENT = "clients";

    @Autowired
    private MongoTemplate mongoTemplate;

    public ClientEntity findEnabledClientByMobile(String mobileNumber, String ISDCode) {
        ClientEntity client = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("mobile").is(mobileNumber));
            query.addCriteria(Criteria.where("isd").is(ISDCode));
            query.addCriteria(Criteria.where("is_disabled").is(false));
            client = mongoTemplate.findOne(query,
                    ClientEntity.class, "clients");
            return client;
        } catch (Exception e) {
            log.error("ClientRepository.findClientByMobile", e);
            return client;
        }
    }

    public ClientEntity findEnabledClientByClientId(String clientId) {
        ClientEntity client = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("client_id").is(clientId));
            query.addCriteria(Criteria.where("is_disabled").is(false));
            client = mongoTemplate.findOne(query,
                    ClientEntity.class, "clients");
            return client;
        } catch (Exception e) {
            log.error("ClientRepository.findEnabledClientByClientId", e);
            return client;
        }
    }

    public ClientEntity findEnabledClientByEmailPassword(String email, String encryptedPassword) {
        ClientEntity client = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("email").is(email));
            query.addCriteria(Criteria.where("password").is(encryptedPassword));
            query.addCriteria(Criteria.where("is_disabled").is(false));
            query.collation(Collation.of("en").
                    strength(Collation.ComparisonLevel.secondary()));
            client = mongoTemplate.findOne(query,
                    ClientEntity.class, "clients");
            return client;
        } catch (Exception e) {
            log.error("ClientRepository.findEnabledClientByEmailPassword", e);
            return client;
        }
    }

    public ClientEntity findEnabledClientByEmail(String email) {
        ClientEntity client = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("email").is(email));
            query.addCriteria(Criteria.where("is_disabled").is(false));
            query.collation(Collation.of("en").
                    strength(Collation.ComparisonLevel.secondary()));
            client = mongoTemplate.findOne(query,
                    ClientEntity.class, "clients");
            return client;
        } catch (Exception e) {
            log.error("ClientRepository.findEnabledClientByEmail", e);
            return client;
        }
    }

    public void bulkUpsert(List<ClientEntity> clientEntities) {
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, ClientEntity.class, "clients");
        for (ClientEntity client : clientEntities) {
            Document doc = new Document();
            mongoTemplate.getConverter().write(client, doc);
            Query query = new Query(Criteria.where("mobile").is(doc.get("mobile")));
            Document updateDoc = new Document();
            updateDoc.append("$set", doc);
            Update update = Update.fromDocument(updateDoc, "_id");
            bulkOps.upsert(query, update);
        }
        bulkOps.execute();
    }

    public void bulkUpsertByClientId(List<ClientEntity> clientEntities) {
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, ClientEntity.class, "clients");
        for (ClientEntity client : clientEntities) {
            Document doc = new Document();
            mongoTemplate.getConverter().write(client, doc);
            Query query = new Query(Criteria.where("client_id").is(doc.get("client_id")));
            Document updateDoc = new Document();
            updateDoc.append("$set", doc);
            Update update = Update.fromDocument(updateDoc, "_id");
            bulkOps.upsert(query, update);
        }
        bulkOps.execute();

    }

    public List<ClientEntity> findEnabledClientsByCompany(String companyId) {
        List<ClientEntity> clients = new LinkedList<>();
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("company_roles.company_id").is(companyId));
            query.addCriteria(Criteria.where("is_disabled").is(false));

            // using sort to pick latest created order in entityMap in case of duplicate entries
            query.with((Sort.by(Sort.Direction.ASC, "companyroles.role")));
            clients = mongoTemplate.find(query,
                    ClientEntity.class, "clients");
            return clients;
        } catch (Exception e) {
            log.error("ClientRepository.findEnabledClientsByCompany", e);
            return clients;
        }
    }

    public void updateImage(String clientId, String imageURL) {
        try {
            Query query = new Query(Criteria.where("client_id").is(clientId));
            Update update = new Update();
            update.set("profile_image", imageURL);
            mongoTemplate.updateFirst(query, update, ClientEntity.class);
        } catch (Exception e) {
            log.error("ClientRepository.updateImage", e);
        }
    }

    public void updateCompanyRoles(String clientId, List<UserCompanyRole> userCompanyRoles) {
        try {
            Query query = new Query(Criteria.where("client_id").is(clientId));
            Update update = new Update();
            update.set("company_roles", userCompanyRoles);
            mongoTemplate.updateFirst(query, update, ClientEntity.class);
        } catch (Exception e) {
            log.error("ClientRepository.updateCompanyRoles", e);
        }
    }

    public List<ClientEntity> getAllClient() {
        List<ClientEntity> result = new LinkedList<>();
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("is_disabled").is(false));
            result = mongoTemplate.findAll(ClientEntity.class, CLIENT);
            return result;
        } catch (Exception e) {
            log.error("ClientRepository.getAllClient", e);
            return result;
        }
    }
    public ClientEntity getClientById(String clientId) {
        ClientEntity client = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("client_id").is(clientId));
            query.addCriteria(Criteria.where("is_disabled").is(false));
            client = mongoTemplate.findOne(query,
                    ClientEntity.class, CLIENT);
            return client;
        } catch (Exception e) {
            log.error("InternalClientRepository.getClientById", e);
            return client;
        }
    }

    public ClientEntity findClientByClientId(String clientId){
        ClientEntity client = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("client_id").is(clientId));
            client = mongoTemplate.findOne(query,
                    ClientEntity.class, "clients");
            return client;
        } catch (Exception e) {
            log.error("ClientRepository.findClientByClientId", e);
            return client;
        }
    }

    public void uploadOnesignalToken(String clientId, String onesignalToken){
        Query query = new Query();
        Update update = new Update();
        try{
            query.addCriteria(Criteria.where("client_id").is(clientId));
            ClientEntity client = mongoTemplate.findOne(query, ClientEntity.class, CLIENT);
            if(Objects.nonNull(client)){
                update.set("onesignal_token", onesignalToken);
                mongoTemplate.updateFirst(query, update, CLIENT);
            }else{
                log.error("Client not found");
            }
        } catch (Exception e){
            log.error("ClientRepository.uploadOnesignalToken", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public ClientEntity findByEmail(String emailId) {
        ClientEntity client = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("email").is(emailId));
            client = mongoTemplate.findOne(query,
                    ClientEntity.class, "clients");

        } catch (Exception e) {
            log.error("[ClientRepository:findByEmail] Error While Getting Fetch data from db . {}", e.getMessage());
            e.printStackTrace();
        }
        return client;
    }
}
