package com.evertix.financialwallet.service;

import com.evertix.financialwallet.controller.commons.MessageResponse;
import com.evertix.financialwallet.model.Discount;
import com.evertix.financialwallet.model.Rate;
import com.evertix.financialwallet.model.request.DiscountRequest;
import org.springframework.http.ResponseEntity;

public interface DiscountService extends FinancialService{
    ResponseEntity<MessageResponse> addDiscount(DiscountRequest discount, Long rateId);
    void financialOperation(DiscountRequest request, Discount discount, Rate rate);
}
