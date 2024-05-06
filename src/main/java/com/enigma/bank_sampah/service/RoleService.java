package com.enigma.bank_sampah.service;

import com.enigma.bank_sampah.constant.UserRole;
import com.enigma.bank_sampah.entity.Role;

public interface RoleService {
    Role getOrSave(UserRole role);
}
