package com.evertix.financialwallet.model.request;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DiscountRequest {
    @Column(nullable = false, updatable = false)
    private LocalDate initialAt;

    @Column(nullable = false, updatable = false)
    private LocalDate expirationAt;

    @DecimalMin(value = "0.00")
    @Digits(integer = 5, fraction = 2)
    private BigDecimal valueNominal;

    @DecimalMin(value = "0.00")
    @Digits(integer = 5, fraction = 2)
    private BigDecimal retention;
}
