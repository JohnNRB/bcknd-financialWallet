package com.evertix.financialwallet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "rates")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Rate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Max(value = 365)
    private Integer daysYear;

    @NotNull(message = "Period Rate cannot be null")
    @NotBlank(message = "Period Rate cannot be blank")
    @Size(max = 40)
    private String periodRate;

    @Max(value = 365)
    private Integer daysRate;

    @DecimalMin(value = "0.00")
    @Digits(integer = 2, fraction = 2)
    private BigDecimal valueRate;

    @NotNull(message = "Period Rate cannot be null")
    @NotBlank(message = "Period Rate cannot be blank")
    @Size(max = 40)
    private String periodCapitalization;

    @Max(value = 365)
    private Integer daysCapitalization;

    @Column(nullable = false, updatable = false)
    private LocalDate discountAt;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "typeRate_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TypeRate typeRate;

    public Rate(Integer daysYear, String periodRate, Integer daysRate, BigDecimal valueRate,
                String periodCapitalization, Integer daysCapitalization, LocalDate discountAt) {
        this.daysYear = daysYear;
        this.periodRate = periodRate;
        this.daysRate = daysRate;
        this.valueRate = valueRate;
        this.periodCapitalization = periodCapitalization;
        this.daysCapitalization = daysCapitalization;
        this.discountAt = discountAt;
    }
}
