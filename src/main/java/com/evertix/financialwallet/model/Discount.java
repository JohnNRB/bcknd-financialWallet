package com.evertix.financialwallet.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "discounts")
@NoArgsConstructor
@Getter
@Setter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "rade_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Rate rate;

    @Max(value = 365)
    private Integer dayPeriod;

    @DecimalMin(value = "0.00")
    @Digits(integer = 3, fraction = 7)
    private BigDecimal rateEffective;

    @DecimalMin(value = "0.00")
    @Digits(integer = 3, fraction = 7)
    private BigDecimal rateDiscount;

    @DecimalMin(value = "0.00")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal valueDiscount;

    @DecimalMin(value = "0.00")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal valueNet;

    @DecimalMin(value = "0.00")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal valueReceived;

    @DecimalMin(value = "0.00")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal valueDelivered;

    @DecimalMin(value = "0.00")
    @Digits(integer = 3, fraction = 7)
    private BigDecimal TCEA;

    public Discount(LocalDate initialAt, LocalDate expirationAt, BigDecimal valueNominal, BigDecimal retention) {
        this.initialAt = initialAt;
        this.expirationAt = expirationAt;
        this.valueNominal = valueNominal;
        this.retention = retention;
    }
}
