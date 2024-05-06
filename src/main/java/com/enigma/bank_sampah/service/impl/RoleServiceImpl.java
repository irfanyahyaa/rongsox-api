package com.enigma.bank_sampah.service.impl;

import com.enigma.bank_sampah.constant.UserRole;
import com.enigma.bank_sampah.entity.Role;
import com.enigma.bank_sampah.repository.RoleRepository;
import com.enigma.bank_sampah.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Role getOrSave(UserRole role) {
        return roleRepository.findByRole(role)
                .orElseGet(() -> roleRepository.saveAndFlush(Role.builder().role(role).build()));
    }
}
