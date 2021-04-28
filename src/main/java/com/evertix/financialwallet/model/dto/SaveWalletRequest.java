package com.evertix.financialwallet.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class SaveWalletRequest {
    private String currency;
    private BigDecimal valueTotalReceived;
    private BigDecimal valueTCEA;
    private String typeWalletName;
    private String enterpriseName;
}
