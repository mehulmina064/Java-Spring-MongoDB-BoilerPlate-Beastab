package com.beastab.dataservice.identityservice.service;

import com.beastab.dataservice.common.utils.*;
import com.beastab.dataservice.identityservice.exceptions.*;
import com.beastab.dataservice.identityservice.request.*;
import com.beastab.dataservice.identityservice.response.*;
import com.beastab.dataservice.communicationservice.request.OTPRequest;
import com.beastab.dataservice.communicationservice.request.VerifyOTPRequest;
import com.beastab.dataservice.communicationservice.response.OTPResponse;
import com.beastab.dataservice.communicationservice.response.VerifyOTPResponse;
import com.beastab.dataservice.communicationservice.service.ISMSService;
import com.beastab.dataservice.identityservice.DTO.Client;
import com.beastab.dataservice.identityservice.DTO.ClientTeam;
import com.beastab.dataservice.identityservice.DTO.Company;
import com.beastab.dataservice.identityservice.DTO.UserCompanyRole;
import com.beastab.dataservice.identityservice.db.entity.ClientEntity;
import com.beastab.dataservice.identityservice.db.entity.CompanyEntity;
import com.beastab.dataservice.identityservice.db.entity.EmailerEntity;
import com.beastab.dataservice.identityservice.db.repository.ClientRepository;
import com.beastab.dataservice.identityservice.db.repository.CompanyRepository;
import com.beastab.dataservice.identityservice.db.repository.EmailerRepository;
import com.beastab.dataservice.identityservice.enums.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientService {

    public static final String COMPANY_ID = "company_id";
    public static final String CLIENT_ID = "client_id";
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CompanyRepository companyRepository;


    @Autowired
    private JWTUtils jwtUtil;

    @Autowired
    private ISMSService smsService;

    @Autowired
    private AuditUtil auditUtil;

    @Autowired
    private EmailerRepository emailerRepository;



    public ResponseEntity<String> register(ClientRegisterationRequest clientRegisterationRequest, String source) {
        if (StringUtils.isNotEmpty(clientRegisterationRequest.getISDCode()) && clientRegisterationRequest.getISDCode().length() > 0
                && clientRegisterationRequest.getISDCode().charAt(0) == '+') {
            clientRegisterationRequest.setISDCode(clientRegisterationRequest.getISDCode().substring(1));
        }
        ClientEntity clientEntity = clientRepository.findEnabledClientByMobile(clientRegisterationRequest.getMobile(),
                clientRegisterationRequest.getISDCode());
        if (Objects.nonNull(clientEntity)) {
            if (StringUtils.equalsIgnoreCase(clientEntity.getMobile(), clientRegisterationRequest.getMobile()) &&
                    StringUtils.equalsIgnoreCase(clientEntity.getISDCode(), clientRegisterationRequest.getISDCode())) {
                return new ResponseEntity<>("Mobile number already registered",HttpStatus.CONFLICT);
            }
        }
        //new user
        CompanyEntity companyEntity = getCompanyEntity(clientRegisterationRequest);
        if (Objects.nonNull(companyEntity)) {
            clientEntity = new ClientEntity(clientRegisterationRequest, companyEntity.getCompanyId(), source);
            if (Boolean.TRUE.equals(clientRegisterationRequest.getIsVerifyFromEmail())) {
                clientEntity.setEmailVerified(true);
            }
            List<ClientEntity> clientEntities = new ArrayList<>();
            clientEntities.add(clientEntity);
            clientRepository.bulkUpsert(clientEntities);
            sendMail(clientEntities,companyEntity.getName(),source);
            return new ResponseEntity<String>("User registered successfully", HttpStatus.OK);
        }
        throw new UserRegistrationException("User registration failed");
    }

    public void sendMail(List<ClientEntity> clientEntities,String companyName,String source){
        List<String> mailGroupIds = Arrays.asList("id");
        List<EmailerEntity> emailerEntity = emailerRepository.findByMultipleMailerIds(mailGroupIds);
        List<String> toSendEmailIds = emailerEntity.parallelStream()
                .flatMap(emailer -> emailer.getToList().stream())
                .collect(Collectors.toList());
        List<String> toCCEmailIds = emailerEntity.parallelStream()
                .flatMap(emailer -> emailer.getCcList().stream())
                .collect(Collectors.toList());
        // Construct the DataString using information from the list of clients
        StringBuilder dataString = new StringBuilder();
        dataString.append("Company Name: ").append(companyName).append("\n");
        dataString.append("Source of Signup: ").append(source).append("\n");
        for (ClientEntity client : clientEntities) {
            dataString.append("Client Name: ").append(client.getName()).append("\n");
            dataString.append("Client Phone Number: ").append(client.getMobile()).append("\n");
            dataString.append("\n"); // Add a separator between client information
        }

    }

    public ResponseEntity<ClientLoginResponse> login(ClientLoginRequest loginRequest, String source) {
        String email = loginRequest.getEmail().trim();
        ClientEntity clientEntity = clientRepository.findEnabledClientByEmail(email);
        String encryptedPassword = EncryptionUtils.encryptPassword(loginRequest.getPassword());
        if (Objects.nonNull(clientEntity)) {
            if (StringUtils.equalsIgnoreCase(clientEntity.getPassword(), encryptedPassword)) {
                UserCompanyRole userCompanyRole = ClientEntity.getPrimaryUserCompanyRole(clientEntity);
                if (ObjectUtils.isEmpty(userCompanyRole) && ObjectUtils.isEmpty(userCompanyRole.getCompanyID())) {
                    auditUtil.audit(source, "LOGIN_WITH_EMAIL", email,
                            loginRequest.getPassword(), "", clientEntity.getClientId(), "COMPANY_ISSUE", false);
                    throw new LoginException("Incorrect Mail");
                }
                CompanyEntity company = companyRepository.findCompanyById(userCompanyRole.getCompanyID());
                auditUtil.audit(source, "LOGIN_WITH_EMAIL", email,
                        loginRequest.getPassword(), "", clientEntity.getClientId(), "LOGIN_SUCCESSFUL", true);
                return createClientLoginResponse(clientEntity, company.getName());
            } else {
                auditUtil.audit(source, "LOGIN_WITH_EMAIL", email,
                        loginRequest.getPassword(), "", clientEntity.getClientId(), "INCORRECT_PASSWORD", false);
                throw new LoginException("Incorrect Password");
            }
        } else {
            auditUtil.audit(source, "LOGIN_WITH_EMAIL", email,
                    loginRequest.getPassword(), "", "", "EMAIL_NOT_REGISTERED", false);
            throw new LoginException("Email not registered");
        }
    }

    public ResponseEntity<ClientSendOTPResponse> sendOTPWithClientCheck(OTPRequest otpRequest) {
        if (StringUtils.isEmpty(otpRequest.getISDCode()))
            otpRequest.setISDCode("91");
        ClientEntity clientEntity = clientRepository.findEnabledClientByMobile(otpRequest.getMobile(), otpRequest.getISDCode());

        if (otpRequest.isRegistration()) {
            if (!Objects.nonNull(clientEntity)) {
                //client exists
                ResponseEntity<OTPResponse> sendOTPResponse = smsService.sendOTP(otpRequest.getMobile(), otpRequest.getISDCode());
                if (sendOTPResponse.getStatusCode() == HttpStatus.OK && sendOTPResponse.hasBody() &&
                        "success".equalsIgnoreCase(sendOTPResponse.getBody().getType())) {
                    ClientSendOTPResponse clientSendOTPResponse =
                            new ClientSendOTPResponse("SUCCESS", "OTP sent successfully", "");
                    return new ResponseEntity<ClientSendOTPResponse>(clientSendOTPResponse, HttpStatus.OK);
                }
            }
            //client doesn't exist
            ClientSendOTPResponse clientSendOTPResponse =
                    new ClientSendOTPResponse("FAILED", "Mobile number already associated with another account", null);
            return new ResponseEntity<ClientSendOTPResponse>(clientSendOTPResponse, HttpStatus.FORBIDDEN);
        } else {
            if (Objects.nonNull(clientEntity)) {
                //client exists
                ResponseEntity<OTPResponse> sendOTPResponse = smsService.sendOTP(otpRequest.getMobile(), otpRequest.getISDCode());
                if (sendOTPResponse.getStatusCode() == HttpStatus.OK && sendOTPResponse.hasBody() &&
                        "success".equalsIgnoreCase(sendOTPResponse.getBody().getType())) {
                    ClientSendOTPResponse clientSendOTPResponse =
                            new ClientSendOTPResponse("SUCCESS", "OTP sent successfully", clientEntity.getClientId());
                    return new ResponseEntity<ClientSendOTPResponse>(clientSendOTPResponse, HttpStatus.OK);
                }
            }
            //client doesn't exist
            ClientSendOTPResponse clientSendOTPResponse =
                    new ClientSendOTPResponse("FAILED", "Mobile number not associated with any account", null);
            return new ResponseEntity<ClientSendOTPResponse>(clientSendOTPResponse, HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<ClientLoginResponse> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        ClientEntity clientEntity = clientRepository.findEnabledClientByClientId(resetPasswordRequest.getClientId());
        if (Objects.nonNull(clientEntity)) {
            VerifyOTPRequest verifyOTPRequest = new VerifyOTPRequest(resetPasswordRequest.getOTP(), clientEntity.getMobile(), clientEntity.getISDCode());
            //client exists
            ResponseEntity<VerifyOTPResponse> sendOTPResponse = smsService.verifyOTP(verifyOTPRequest);
            if (sendOTPResponse.getStatusCode() == HttpStatus.OK && sendOTPResponse.hasBody() &&
                    "success".equalsIgnoreCase(sendOTPResponse.getBody().getType())) {
                ClientEntity.updatePassword(clientEntity, resetPasswordRequest.getPassword());
                List<ClientEntity> clientEntities = new ArrayList<>();
                clientEntities.add(clientEntity);
                clientRepository.bulkUpsert(clientEntities);
                UserCompanyRole userCompanyRole = ClientEntity.getPrimaryUserCompanyRole(clientEntity);
                CompanyEntity company = companyRepository.findCompanyById(userCompanyRole.getCompanyID());
                return createClientLoginResponse(clientEntity, company.getName());
            }
        }
        //client doesn't exist
        return getFailedLoginResponse();
    }

    public ResponseEntity<ClientLoginResponse> verifyOTP(ClientOTPLoginRequest resetPasswordRequest, String source) {
        ClientEntity clientEntity = clientRepository.findEnabledClientByClientId(resetPasswordRequest.getClientId());
        if (Objects.nonNull(clientEntity)) {
            VerifyOTPRequest verifyOTPRequest = new VerifyOTPRequest(resetPasswordRequest.getOtp(), clientEntity.getMobile(), clientEntity.getISDCode());
            ResponseEntity<VerifyOTPResponse> sendOTPResponse = smsService.verifyOTP(verifyOTPRequest);
            if (sendOTPResponse.getStatusCode() == HttpStatus.OK && sendOTPResponse.hasBody() &&
                    "success".equalsIgnoreCase(sendOTPResponse.getBody().getType())) {
                UserCompanyRole userCompanyRole = ClientEntity.getPrimaryUserCompanyRole(clientEntity);
                CompanyEntity company = companyRepository.findCompanyById(userCompanyRole.getCompanyID());
                auditUtil.audit(source, "LOGIN_WITH_MOBILE_OTP", "",
                        resetPasswordRequest.getOtp(), clientEntity.getMobile(), clientEntity.getClientId(), "LOGIN_SUCCESSFUL", true);
                return createClientLoginResponse(clientEntity, company.getName());
            }
        }
        auditUtil.audit(source, "LOGIN_WITH_MOBILE_OTP", "",
                resetPasswordRequest.getOtp(), "", resetPasswordRequest.getClientId(), "INCORRECT_OTP", false);
        return getFailedLoginResponse();
    }

    public ResponseEntity<String> updateImage(MultipartFile file, HashMap<String, String> auth) {
        String clientId = auth.getOrDefault("client_id", "");
        if (StringUtils.isNoneEmpty(clientId)) {
            ClientEntity clientEntity = clientRepository.findEnabledClientByClientId(clientId);
            if (Objects.nonNull(clientEntity)) {
                String fileName = clientId + "_" + UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            }
        }
        return new ResponseEntity<String>("Failed to update", HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<ClientTeamResponse> addTeamMember(HashMap<String, String> auth,
                                                            AddTeamMemberRequest addTeamMemberRequest) {
        if (StringUtils.isEmpty(addTeamMemberRequest.getIsd()))
            addTeamMemberRequest.setIsd("91");
        ClientEntity clientEntity =
                clientRepository.findEnabledClientByMobile(addTeamMemberRequest.getMobile(), addTeamMemberRequest.getIsd());
        if (Objects.nonNull(clientEntity)) {
            List<UserCompanyRole> userCompanyRoles = clientEntity.getCompanyRoles();
            userCompanyRoles.add(new UserCompanyRole(addTeamMemberRequest, auth.get(COMPANY_ID)));
            clientRepository.updateCompanyRoles(clientEntity.getClientId(), userCompanyRoles);
            return getMyTeam(auth);
        } else {
            clientEntity = new ClientEntity(addTeamMemberRequest, auth.get(COMPANY_ID));
            List<ClientEntity> clientEntityList = new ArrayList<>();
            clientEntityList.add(clientEntity);
            clientRepository.bulkUpsert(clientEntityList);
            return getMyTeam(auth);
        }
    }

    private static ResponseEntity<ClientLoginResponse> getFailedLoginResponse() {
        ClientLoginResponse clientLoginResponse =
                new ClientLoginResponse("failed", "null", null);
        return new ResponseEntity<ClientLoginResponse>(clientLoginResponse, HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<ClientLoginResponse> switchCompany(HashMap<String, String> auth,
                                                             @RequestBody String companyId) {
        String clientId = auth.getOrDefault("client_id", "");
        if (StringUtils.isNoneEmpty(clientId)) {
            ClientEntity clientEntity = clientRepository.findEnabledClientByClientId(clientId);
            for (UserCompanyRole companyRole : clientEntity.getCompanyRoles()) {
                if (StringUtils.equalsIgnoreCase(companyRole.getCompanyID(), companyId)) {
                    CompanyEntity company = companyRepository.findCompanyById(companyRole.getCompanyID());
                    Client client = new Client(clientEntity, companyId, company.getName());
                    ClientLoginResponse clientLoginResponse = new ClientLoginResponse("success", jwtUtil.generateToken(client), client);
                    return new ResponseEntity<ClientLoginResponse>(clientLoginResponse, HttpStatus.OK);
                }
            }
        }
        return getFailedLoginResponse();
    }

    public ResponseEntity<?> getCompanies(HashMap<String, String> auth) {
        String clientId = auth.getOrDefault("client_id", "");
        ClientEntity clientEntity = clientRepository.findEnabledClientByClientId(clientId);
        if (ObjectUtils.isNotEmpty(clientEntity)) {
            List<Company> companies = new ArrayList<>();
            if (ObjectUtils.isNotEmpty(clientEntity.getCompanyRoles())) {
                for (UserCompanyRole companyRole : clientEntity.getCompanyRoles()) {
                    CompanyEntity company = companyRepository.findCompanyById(companyRole.getCompanyID());
                    companies.add(new Company(company.getCompanyId(), company.getName()));
                }
                return new ResponseEntity<>(new CompaniesResponse(companies), HttpStatus.OK);
            }
            return new ResponseEntity<>("Company Role Not Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Client Not Found", HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<ClientTeamResponse> getMyTeam(HashMap<String, String> auth) {
        String clientId = auth.getOrDefault("client_id", "");
        String companyId = auth.getOrDefault(COMPANY_ID, "");
        if (StringUtils.isNoneEmpty(clientId)) {
            ClientEntity clientEntity = clientRepository.findEnabledClientByClientId(clientId);
//            if (isAdmin(clientEntity, companyId)) {
                List<ClientEntity> clientEntities = clientRepository.findEnabledClientsByCompany(companyId);
                List<ClientTeam> clientTeamResponses =
                        clientEntities.parallelStream().map(c -> new ClientTeam(c, companyId)).collect(Collectors.toList());
                ClientTeamResponse clientTeamResponse = new ClientTeamResponse(clientTeamResponses);
                return new ResponseEntity<ClientTeamResponse>(clientTeamResponse, HttpStatus.OK);

//            }
        }
        throw new FetchTeamException("An error occured while trying to fetch team");
    }

    public ResponseEntity<String> updateTeamMember(HashMap<String, String> auth,
                                                   String clientIdToPromote, UserRole role) {
        String clientId = auth.getOrDefault("client_id", "");
        String companyId = auth.getOrDefault(COMPANY_ID, "");
        if (StringUtils.isNoneEmpty(clientId)) {
            ClientEntity clientEntity = clientRepository.findEnabledClientByClientId(clientId);
            if (isAdmin(clientEntity, companyId)) {
                List<ClientEntity> promotionList = new ArrayList<>();
                ClientEntity clientToPromote = clientRepository.findEnabledClientByClientId(clientIdToPromote);
                for (UserCompanyRole userCompanyRole : clientToPromote.getCompanyRoles()) {
                    if (StringUtils.equalsIgnoreCase(userCompanyRole.getCompanyID(), companyId)) {
                        userCompanyRole.setRole(role);
                        break;
                    }
                }
                promotionList.add(clientToPromote);
                clientRepository.bulkUpsert(promotionList);
                return new ResponseEntity<String>("Updated successfully", HttpStatus.OK);
            }
        }
        throw new MakeAdminException("An error occured while updating user");
    }

    private ResponseEntity<ClientLoginResponse> createClientLoginResponse(ClientEntity clientEntity, String companyName) {
        Client client = new Client(clientEntity, companyName);
        ClientLoginResponse clientLoginResponse = new ClientLoginResponse("success", jwtUtil.generateToken(client), client);
        return new ResponseEntity<ClientLoginResponse>(clientLoginResponse, HttpStatus.OK);
    }

    private CompanyEntity getCompanyEntity(ClientRegisterationRequest clientRegisterationRequest) {
        CompanyEntity companyEntity = companyRepository.findCompanyByName(clientRegisterationRequest.getCompany().toUpperCase());
        if (Objects.nonNull(companyEntity))
            return companyEntity;
        //create company
        CompanyEntity newCompanyEntity = new CompanyEntity(UUID.randomUUID().toString(),
                clientRegisterationRequest.getCompany().toUpperCase(), null, null);
        newCompanyEntity.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        newCompanyEntity.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        List<CompanyEntity> companyEntityList = new ArrayList<>();
        companyEntityList.add(newCompanyEntity);
        boolean response = companyRepository.addCompanies(companyEntityList);
        if (response)
            return newCompanyEntity;

        throw new CompanyCreationException("Unknown error while creating company");
    }

    public String editClient(String id, ClientUpdateRequest clientUpdateRequest) {
        ClientEntity entity = clientRepository.findEnabledClientByClientId(id);

        if (Objects.nonNull(entity)) {
            //client exist
            ClientEntity.updateClientEntity(entity, clientUpdateRequest);
            List<ClientEntity> clientEntityList = new ArrayList<>();
            clientEntityList.add(entity);
            clientRepository.bulkUpsertByClientId(clientEntityList);
            return "Client Upserted";
        }
        throw new FetchTeamException("Client does not exist");
    }

    public ResponseEntity<ClientLoginResponse> editClientForWeb(String id, ClientUpdateRequest clientUpdateRequest) {
        ClientEntity entity = clientRepository.findEnabledClientByClientId(id);

        if (Objects.nonNull(entity)) {
            //client exist
            ClientEntity.updateClientEntity(entity, clientUpdateRequest);
            List<ClientEntity> clientEntityList = new ArrayList<>();
            clientEntityList.add(entity);
            clientRepository.bulkUpsertByClientId(clientEntityList);
            CompanyEntity company = companyRepository.findCompanyById(clientUpdateRequest.getCompanyId());
            if(ObjectUtils.isNotEmpty(company)){
                return createClientLoginResponse(entity, company.getName());
            }
            else{
                throw new FetchTeamException("Company does not exist");
            }
        }
        throw new FetchTeamException("Client does not exist");
    }

    public ResponseEntity<ClientProfileResponse> getProfile(String clientId, HashMap<String, String> auth) {
        String companyId = auth.getOrDefault(COMPANY_ID, "");
        ClientEntity clientEntity = clientRepository.findEnabledClientByClientId(clientId);
        ClientProfileResponse cpr = new ClientProfileResponse(clientEntity, companyId);
        return new ResponseEntity<ClientProfileResponse>(cpr, HttpStatus.OK);
    }

    public ResponseEntity<String> deleteProfile(HashMap<String, String> auth) {
        String clientId = auth.getOrDefault(CLIENT_ID, "");
        return deleteProfile(clientId);
    }

    public ResponseEntity<String> deleteProfile(String clientId) {
        ClientEntity clientEntity = clientRepository.findEnabledClientByClientId(clientId);
        if (Objects.isNull(clientEntity))
            return new ResponseEntity<>("Already deleted", HttpStatus.OK);
        else {
            clientEntity.setDisabled(true);
            List<ClientEntity> clientEntities = new ArrayList<>();
            clientEntities.add(clientEntity);
            clientRepository.bulkUpsert(clientEntities);
            return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
        }
    }

    private boolean isAdmin(ClientEntity clientEntity, String companyId) {
        if (Objects.isNull(clientEntity))
            return false;
        if (!CollectionUtils.isEmpty(clientEntity.getCompanyRoles())) {
            for (UserCompanyRole userCompanyRole : clientEntity.getCompanyRoles()) {
                if (StringUtils.equalsIgnoreCase(userCompanyRole.getCompanyID(), companyId) &&
                        UserRole.ADMIN == userCompanyRole.getRole()) {
                    return true;
                }
            }
        }
        return false;
    }




    public ResponseEntity<ClientResponse> getAllClient(HashMap<String, String> auth) {
        String clientId = auth.getOrDefault(CLIENT_ID, "");

        List<Client> clientList = new LinkedList<>();

        List<ClientEntity> Entities = clientRepository.getAllClient();

        if (!CollectionUtils.isEmpty(Entities)) {
            clientList = Entities.parallelStream().map(Client::new).collect(Collectors.toList());
        }
        ClientResponse clientResponse = ClientResponse.builder().clients(clientList).build();
        return ResponseEntity.ok(clientResponse);

    }

}
