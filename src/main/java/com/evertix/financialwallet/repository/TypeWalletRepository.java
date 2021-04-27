package com.evertix.financialwallet.repository;

import com.evertix.financialwallet.model.TypeWallet;
import com.evertix.financialwallet.model.emuns.EWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeWalletRepository extends JpaRepository<TypeWallet, Long> {
    Optional<TypeWallet> findAllByName(EWallet name);
}
