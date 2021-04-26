package com.evertix.financialwallet.repository;

import com.evertix.financialwallet.model.Role;
import com.evertix.financialwallet.model.emuns.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
