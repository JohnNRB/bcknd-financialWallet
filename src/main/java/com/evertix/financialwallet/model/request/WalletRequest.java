package com.evertix.financialwallet.model.request;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class WalletRequest {
    @NotNull(message = "Currency cannot be null")
    @NotBlank(message = "Currency cannot be blank")
    @Size(max = 30)
    private String currency;

    @DecimalMin(value = "0.00")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal valueTotalReceived;

    @DecimalMin(value = "0.00")
    @Digits(integer = 3, fraction = 7)
    private BigDecimal valueTCEA;
}
