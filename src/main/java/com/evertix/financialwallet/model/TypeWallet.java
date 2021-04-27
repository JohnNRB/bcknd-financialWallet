package com.evertix.financialwallet.model;

import com.evertix.financialwallet.model.emuns.EWallet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "type_wallets")
@NoArgsConstructor
@Getter
@Setter
public class TypeWallet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private EWallet name;

    public TypeWallet(EWallet name){ this.name = name; }
}
