package com.evertix.financialwallet.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SaveRateRequest {
    private String periodRate;
    private Integer daysRate;
    private BigDecimal valueRate;
    private String periodCapitalization;
    private Integer daysCapitalization;
    private LocalDate discountAt;
    private String typeRateName;
}
