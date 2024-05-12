package com.enigma.bank_sampah.service;

import com.enigma.bank_sampah.dto.request.SearchAdminRequest;
import com.enigma.bank_sampah.dto.request.UpdateAdminRequest;
import com.enigma.bank_sampah.dto.response.AdminResponse;
import com.enigma.bank_sampah.entity.Admin;
import org.springframework.data.domain.Page;

public interface AdminService {
    Admin create(Admin admin);

    Page<AdminResponse> getAll(SearchAdminRequest request);

    Admin getByIdEntity(String id);

    Admin getByUserAccountId(String id);

    AdminResponse update(UpdateAdminRequest request);

    void updateStatusById(String id, Boolean status);

    void deleteById(String id);

    void findByPhoneNumber(String phoneNumber);
}
