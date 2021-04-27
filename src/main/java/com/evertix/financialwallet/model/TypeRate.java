package com.evertix.financialwallet.model;

import com.evertix.financialwallet.model.emuns.ERate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "type_rates")
@NoArgsConstructor
@Getter
@Setter
public class TypeRate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERate name;

    public TypeRate(ERate name) { this.name = name; }
}
