package com.enigma.bank_sampah.service.impl;

import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.dto.request.SearchAdminRequest;
import com.enigma.bank_sampah.dto.response.AdminResponse;
import com.enigma.bank_sampah.entity.Admin;
import com.enigma.bank_sampah.repository.AdminRepository;
import com.enigma.bank_sampah.service.AdminService;
import com.enigma.bank_sampah.service.ImageService;
import com.enigma.bank_sampah.specification.AdminSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public Page<AdminResponse> getAll(SearchAdminRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);
        Specification<Admin> specification = AdminSpecification.getSpecification(request);

        Page<Admin> adminPage = adminRepository.findAll(specification, pageable);

        return adminPage.map(user -> AdminResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .position(user.getPosition())
                .phoneNumber(user.getPhoneNumber())
                .image(user.getImage())
                .userAccountId(user.getUserAccount().getId())
                .status(user.getStatus())
                .build());
    }

    @Override
    public Admin getByUserAccountId(String id) {
        return adminRepository.findByUserAccount_Id(id).orElse(null);
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
        updateStatusById(id, false);
    }
}
