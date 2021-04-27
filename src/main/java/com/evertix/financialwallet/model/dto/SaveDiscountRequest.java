package com.evertix.financialwallet.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SaveDiscountRequest {
    private LocalDate initialAt;
    private LocalDate expirationAt;
    private BigDecimal valueNominal;
    private BigDecimal retention;
    private String rateName;
    private Integer dayPeriod;
    private BigDecimal rateEffective;
    private BigDecimal rateDiscount;
    private BigDecimal valueDiscount;
    private BigDecimal valueNet;
    private BigDecimal valueReceived;
    private BigDecimal valueDelivered;
    private BigDecimal TCEA;
}
