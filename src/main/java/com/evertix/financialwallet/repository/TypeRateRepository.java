package com.evertix.financialwallet.repository;

import com.evertix.financialwallet.model.TypeRate;
import com.evertix.financialwallet.model.emuns.ERate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRateRepository extends JpaRepository<TypeRate, Long> {
    Optional<TypeRate> findByName(ERate name);
}
