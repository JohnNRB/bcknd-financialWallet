package com.evertix.financialwallet.repository;

import com.evertix.financialwallet.model.EconomicActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EconomicActivityRepository extends JpaRepository<EconomicActivity, Long> { }
