package com.evertix.financialwallet.repository;

import com.evertix.financialwallet.model.Wallet;
import com.evertix.financialwallet.model.emuns.EWallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findAllById(Long walletId);
    List<Wallet> findAllByTypeWalletNameAndEnterpriseId(EWallet name, Long enterpriseId);
    Page<Wallet> findAllByTypeWalletNameAndEnterpriseId(EWallet name, Long enterpriseId, Pageable pageable);
}
