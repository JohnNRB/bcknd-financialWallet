package com.evertix.financialwallet.repository;

import com.evertix.financialwallet.model.Enterprise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {
    List<Enterprise> findAllByManagerId(Long managerId);
    Page<Enterprise> findAllByManagerId(Long managerId, Pageable pageable);
}
