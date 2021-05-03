package com.evertix.financialwallet.service;

import com.evertix.financialwallet.controller.commons.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface EconomicActivityService {
    ResponseEntity<MessageResponse> getAllEconomicActivities();
}
