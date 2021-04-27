package com.evertix.financialwallet.model.request;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RateRequest {
    @Max(value = 365)
    private Integer daysYear;

    @NotNull(message = "Period Rate cannot be null")
    @NotBlank(message = "Period Rate cannot be blank")
    @Size(max = 40)
    private String periodRate;

    @Max(value = 365)
    private Integer daysRate;

    @DecimalMin(value = "0.00")
    @Digits(integer = 2, fraction = 7)
    private BigDecimal valueRate;

    @NotNull(message = "Period Rate cannot be null")
    @NotBlank(message = "Period Rate cannot be blank")
    @Size(max = 40)
    private String periodCapitalization;

    @Max(value = 365)
    private Integer daysCapitalization;

    @Column(nullable = false, updatable = false)
    private LocalDate discountAt;
}
