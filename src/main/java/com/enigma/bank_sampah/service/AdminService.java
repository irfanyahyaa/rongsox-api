package com.enigma.bank_sampah.service;

import com.enigma.bank_sampah.entity.Admin;

public interface AdminService {
    Admin create(Admin admin);
//    AdminResponse getOne(String id);
//    Page<AdminResponse> getAll(SearchAdminRequest request);
//    AdminResponse update(UpdateAdminRequest request);
    Admin getByUserAccountId(String id);
    void updateStatusById(String id, Boolean status);
    void deleteById(String id);
}
