package com.evertix.financialwallet.service;

import com.evertix.financialwallet.controller.commons.MessageResponse;
import com.evertix.financialwallet.model.request.RateRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface RateService {
    ResponseEntity<MessageResponse> getAllRate();
    ResponseEntity<MessageResponse> getAllRate(String typeRateName);
    ResponseEntity<MessageResponse> getAllRatePaginated(String typeRateName, Pageable pageable);
    ResponseEntity<MessageResponse> addRate(RateRequest rate, String typeRateName);
    ResponseEntity<MessageResponse> updateRate(RateRequest rate, Long rateId);
    ResponseEntity<MessageResponse> deleteRate(Long rateId);
}
