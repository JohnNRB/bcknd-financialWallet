package com.evertix.financialwallet.service;

import com.evertix.financialwallet.controller.commons.MessageResponse;
import com.evertix.financialwallet.model.request.EnterpriseRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface EnterpriseService {
    ResponseEntity<MessageResponse> getAllEnterprises(Long managerId);
    ResponseEntity<MessageResponse> getAllEnterprisesPaginated(Long managerId, Pageable pageable);
    ResponseEntity<MessageResponse> addEnterprise(EnterpriseRequest enterprise, Long economicActivityId, Long managerId);
    ResponseEntity<MessageResponse> updateEnterprise(EnterpriseRequest enterprise, Long economicActivityId, Long enterpriseId);
    ResponseEntity<MessageResponse> deleteEnterprise(Long enterpriseId);
}
