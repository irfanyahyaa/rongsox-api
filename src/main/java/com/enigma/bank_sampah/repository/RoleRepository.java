package com.enigma.bank_sampah.repository;

import com.enigma.bank_sampah.constant.UserRole;
import com.enigma.bank_sampah.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(UserRole role);
}
