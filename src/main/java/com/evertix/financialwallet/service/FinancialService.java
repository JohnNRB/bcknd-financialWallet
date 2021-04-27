package com.evertix.financialwallet.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface FinancialService {
    Integer daysPeriod(LocalDate expiration, LocalDate discount);
    BigDecimal rateEffective(Integer daysPeriod, BigDecimal valueRate);
    BigDecimal rateEffectiveNominal(Integer daysPeriod, BigDecimal valueRate, Integer daysRate, Integer daysCapitalization);
    BigDecimal rateDiscount(BigDecimal rateEffective);
    BigDecimal valueDiscount(BigDecimal valueNominal, BigDecimal rateDiscount);
    BigDecimal valueNet(BigDecimal valueNominal, BigDecimal valueDiscount);
    BigDecimal valueReceived(BigDecimal valueNet, BigDecimal retention);
    BigDecimal valueDelivered(BigDecimal valueNominal, BigDecimal retention);
    BigDecimal TCEA(BigDecimal valueReceived, BigDecimal valueDelivered, Integer daysPeriod, Integer daysYear);
}
