package com.enigma.bank_sampah.service.impl;

import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.dto.request.SearchAdminRequest;
import com.enigma.bank_sampah.dto.request.UpdateAdminRequest;
import com.enigma.bank_sampah.dto.response.AdminResponse;
import com.enigma.bank_sampah.entity.Admin;
import com.enigma.bank_sampah.entity.UserAccount;
import com.enigma.bank_sampah.repository.AdminRepository;
import com.enigma.bank_sampah.service.AdminService;
import com.enigma.bank_sampah.service.ImageService;
import com.enigma.bank_sampah.service.UserAccountService;
import com.enigma.bank_sampah.specification.AdminSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final ImageService imageService;
    private final UserAccountService userAccountService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Admin create(Admin admin) {
        return adminRepository.saveAndFlush(admin);
    }

    @Transactional(readOnly = true)
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
                .address(user.getAddress())
                .position(user.getPosition())
                .phoneNumber(user.getPhoneNumber())
                .image(user.getImage())
                .email(user.getUserAccount().getEmail())
                .username(user.getUserAccount().getUsername())
                .status(user.getStatus())
                .build());
    }

    @Transactional(readOnly = true)
    @Override
    public Admin getByIdEntity(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Admin getByUserAccountId(String id) {
        return adminRepository.findByUserAccount_Id(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public AdminResponse getByIdDTO(String id) {
        Admin adminFound = findByIdOrThrowNotFound(id);

        return AdminResponse.builder()
                .id(adminFound.getId())
                .name(adminFound.getName())
                .address(adminFound.getAddress())
                .position(adminFound.getPosition())
                .phoneNumber(adminFound.getPhoneNumber())
                .image(adminFound.getImage())
                .email(adminFound.getUserAccount().getEmail())
                .username(adminFound.getUserAccount().getUsername())
                .status(adminFound.getStatus())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminResponse update(UpdateAdminRequest request) {
        Admin adminFound = findByIdOrThrowNotFound(request.getId());

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(adminFound.getPhoneNumber())) {
            findByPhoneNumber(request.getPhoneNumber());
        } else {
            adminFound.setPhoneNumber(null);
        }

        Admin admin = Admin.builder()
                .id(adminFound.getId())
                .name(request.getName())
                .address(request.getAddress())
                .position(request.getPosition())
                .phoneNumber(request.getPhoneNumber())
                .image(request.getImage())
                .userAccount(adminFound.getUserAccount())
                .status(adminFound.getStatus())
                .build();
        adminRepository.saveAndFlush(admin);

        return AdminResponse.builder()
                .id(admin.getId())
                .name(admin.getName())
                .address(admin.getAddress())
                .position(admin.getPosition())
                .phoneNumber(admin.getPhoneNumber())
                .image(admin.getImage())
                .email(admin.getUserAccount().getEmail())
                .username(admin.getUserAccount().getUsername())
                .status(admin.getStatus())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatusById(String id, Boolean status) {
        Admin adminFound = findByIdOrThrowNotFound(id);
        UserAccount userAccount = userAccountService.getByUserId(adminFound.getUserAccount().getId());

        adminRepository.updateStatus(id, status);
        userAccount.setIsEnable(status);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Admin adminFound = findByIdOrThrowNotFound(id);
        UserAccount userAccount = userAccountService.getByUserId(adminFound.getUserAccount().getId());

        if (adminFound.getImage() != null) {
            imageService.deleteById(adminFound.getImage().getId());
        }

        adminRepository.updateStatus(id, false);
        userAccount.setIsEnable(false);
    }

    private Admin findByIdOrThrowNotFound(String request) {
        return adminRepository.findById(request)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }

    @Override
    public void findByPhoneNumber(String phoneNumber) {
        Optional<Admin> existingPhoneNumber = adminRepository.findAdminByPhoneNumber(phoneNumber);
        if (existingPhoneNumber.isPresent()) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
    }
}
