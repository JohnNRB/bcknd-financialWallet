package com.evertix.financialwallet.service.impl;

import com.evertix.financialwallet.controller.commons.MessageResponse;
import com.evertix.financialwallet.controller.constants.ResponseConstants;
import com.evertix.financialwallet.model.EconomicActivity;
import com.evertix.financialwallet.model.Enterprise;
import com.evertix.financialwallet.model.User;
import com.evertix.financialwallet.model.dto.SaveEnterpriseRequest;
import com.evertix.financialwallet.model.request.EnterpriseRequest;
import com.evertix.financialwallet.repository.EconomicActivityRepository;
import com.evertix.financialwallet.repository.EnterpriseRepository;
import com.evertix.financialwallet.repository.UserRepository;
import com.evertix.financialwallet.service.EnterpriseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@Service
public class EnterpriseServiceImpl implements EnterpriseService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EconomicActivityRepository economicActivityRepository;

    @Autowired
    EnterpriseRepository enterpriseRepository;

    @Override
    public ResponseEntity<MessageResponse> getAllEnterprises(Long managerId) {
        try {
            List<Enterprise> enterpriseList = this.enterpriseRepository.findAllByManagerId(managerId);
            if (enterpriseList == null || enterpriseList.isEmpty()) { return this.getNotEnterpriseContent(); }
            MessageResponse response = MessageResponse.builder()
                    .code(ResponseConstants.SUCCESS_CODE)
                    .message(ResponseConstants.MSG_SUCCESS_CONS)
                    .data(enterpriseList)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.ERROR_CODE)
                            .message("Internal Error: " + sw.toString())
                            .build());
        }
    }

    @Override
    public ResponseEntity<MessageResponse> getAllEnterprisesPaginated(Long managerId, Pageable pageable) {
        try {
            Page<Enterprise> enterprisePage = this.enterpriseRepository.findAllByManagerId(managerId, pageable);
            if (enterprisePage == null || enterprisePage.isEmpty()) { return this.getNotEnterpriseContent(); }
            MessageResponse response = MessageResponse.builder()
                    .code(ResponseConstants.SUCCESS_CODE)
                    .message(ResponseConstants.MSG_SUCCESS_CONS)
                    .data(enterprisePage)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.ERROR_CODE)
                            .message("Internal Error: " + sw.toString())
                            .build());
        }
    }

    @Override
    public ResponseEntity<MessageResponse> addEnterprise(EnterpriseRequest enterprise, Long economicActivityId, Long managerId) {
        try {
            // Validate if Economic Activity Exists
            EconomicActivity economicActivity = this.economicActivityRepository.findById(economicActivityId).orElse(null);
            if (economicActivity == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.builder()
                                .code(ResponseConstants.ERROR_CODE)
                                .message("Don't exists economic activity with ID: " + economicActivityId)
                                .build());
            }

            // Validate if Manager Exists
            User manager = this.userRepository.findById(managerId).orElse(null);
            if (manager == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.builder()
                                .code(ResponseConstants.ERROR_CODE)
                                .message("Don't exists boss with ID: " + managerId)
                                .build());
            }

            // Validate Complete
            Enterprise saveEnterprise = this.convertToEntity(enterprise);
            // Set Economic Activity & Manager
            saveEnterprise.setEconomicActivity(economicActivity);
            saveEnterprise.setManager(manager);
            // Save Enterprise
            enterpriseRepository.save(saveEnterprise);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.SUCCESS_CODE)
                            .message("Successful creation request")
                            .data(this.convertToResource(saveEnterprise))
                            .build());
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.ERROR_CODE)
                            .message("Internal Error: " + sw.toString())
                            .build());
        }
    }

    @Override
    public ResponseEntity<MessageResponse> updateEnterprise(EnterpriseRequest enterprise, Long economicActivityId, Long enterpriseId) {
        try {
            // Validate if Enterprise Exists
            Enterprise saveEnterprise = this.enterpriseRepository.findById(enterpriseId).orElse(null);
            if (saveEnterprise == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.builder()
                                .code(ResponseConstants.ERROR_CODE)
                                .message("Don't exists enterprise with ID: " + enterpriseId)
                                .build());
            }

            // Validate if Economic Activity Exists
            EconomicActivity economicActivity;
            if (economicActivityId != null){
                economicActivity = this.economicActivityRepository.findById(economicActivityId).orElse(null);
                if (economicActivity == null) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(MessageResponse.builder()
                                    .code(ResponseConstants.ERROR_CODE)
                                    .message("Don't exists economic activity with ID: " + economicActivityId)
                                    .build());
                }
            } else {
                economicActivity = saveEnterprise.getEconomicActivity();
            }

            // Update Enterprise Data
            saveEnterprise.setRuc(enterprise.getRuc());
            saveEnterprise.setName(enterprise.getName());
            saveEnterprise.setEmail(enterprise.getEmail());
            saveEnterprise.setAddress(enterprise.getAddress());
            saveEnterprise.setPhone(enterprise.getPhone());
            saveEnterprise.setEconomicActivity(economicActivity);
            // Save Update
            saveEnterprise = enterpriseRepository.save(saveEnterprise);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.SUCCESS_CODE)
                            .message("Successful update")
                            .data(this.convertToResource(saveEnterprise))
                            .build());
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.ERROR_CODE)
                            .message("Internal Error: " + sw.toString())
                            .build());
        }
    }

    @Override
    public ResponseEntity<MessageResponse> deleteEnterprise(Long enterpriseId) {
        try {
            // Validate if Enterprise Exists
            Enterprise enterprise = this.enterpriseRepository.findById(enterpriseId).orElse(null);
            if (enterprise == null){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.builder()
                                .code(ResponseConstants.ERROR_CODE)
                                .message("Don't exists enterprise with ID: " + enterpriseId)
                                .build());
            }

            // Delete Enterprise
            enterpriseRepository.delete(enterprise);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.SUCCESS_CODE)
                            .message("Successful delete")
                            .data(this.convertToResource(enterprise))
                            .build());
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.ERROR_CODE)
                            .message("Internal Error: " + sw.toString())
                            .build());
        }
    }

    private Enterprise convertToEntity(EnterpriseRequest enterprise) { return modelMapper.map(enterprise, Enterprise.class); }

    private SaveEnterpriseRequest convertToResource(Enterprise enterprise) {
        SaveEnterpriseRequest resource = modelMapper.map(enterprise, SaveEnterpriseRequest.class);
        resource.setEconomicActivityName(enterprise.getEconomicActivity().getName());
        resource.setManageName(enterprise.getManager().getLastName() + ", " + enterprise.getManager().getName());
        return resource;
    }

    private ResponseEntity<MessageResponse> getNotEnterpriseContent() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageResponse.builder()
                        .code(ResponseConstants.WARNING_CODE)
                        .message(ResponseConstants.MSG_WARNING_CONS)
                        .data(null)
                        .build());
    }
}
