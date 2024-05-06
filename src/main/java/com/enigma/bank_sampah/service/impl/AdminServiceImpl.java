package com.enigma.bank_sampah.service.impl;

import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.entity.Admin;
import com.enigma.bank_sampah.repository.AdminRepository;
import com.enigma.bank_sampah.service.AdminService;
import com.enigma.bank_sampah.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Admin create(Admin admin) {
        return adminRepository.saveAndFlush(admin);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatusById(String id, Boolean status) {
        findByIdOrThrowNotFound(id);
        adminRepository.updateStatus(id, status);
    }

    private Admin findByIdOrThrowNotFound(String request) {
        return adminRepository.findById(request)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Admin admin = findByIdOrThrowNotFound(id);
        if (admin.getImage() != null) {
            imageService.deleteById(admin.getImage().getId());
        }
        adminRepository.delete(admin);
    }
}
