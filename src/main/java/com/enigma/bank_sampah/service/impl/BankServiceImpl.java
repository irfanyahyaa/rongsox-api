package com.enigma.bank_sampah.service.impl;

import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.dto.request.BankRequest;
import com.enigma.bank_sampah.dto.request.SearchBankRequest;
import com.enigma.bank_sampah.dto.request.UpdateBankRequest;
import com.enigma.bank_sampah.dto.response.BankResponse;
import com.enigma.bank_sampah.entity.Bank;
import com.enigma.bank_sampah.repository.BankRepository;
import com.enigma.bank_sampah.service.BankService;
import com.enigma.bank_sampah.specification.BankSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BankResponse create(BankRequest request) {
        Bank bank = Bank.builder()
                .name(request.getBankName())
                .bankCode(request.getBankCode())
                .build();
        bankRepository.saveAndFlush(bank);

        return BankResponse.builder()
                .id(bank.getId())
                .bankName(bank.getName())
                .bankCode(bank.getBankCode())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BankResponse> getAll(SearchBankRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);
        Specification<Bank> specification = BankSpecification.getSpecification(request.getQuery());

        return bankRepository.findAll(specification, pageable).map(
                bank -> BankResponse.builder()
                        .id(bank.getId())
                        .bankCode(bank.getBankCode())
                        .bankName(bank.getName())
                        .build());
    }

    @Transactional(readOnly = true)
    @Override
    public Bank getByIdEntity(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Transactional(readOnly = true)
    @Override
    public BankResponse getByIdDTO(String id) {
        Bank bankFound = findByIdOrThrowNotFound(id);

        return BankResponse.builder()
                .id(bankFound.getId())
                .bankName(bankFound.getName())
                .bankCode(bankFound.getBankCode())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BankResponse update(UpdateBankRequest request) {
        Bank bankFound = findByIdOrThrowNotFound(request.getId());

        bankFound.setName(request.getBankName());
        bankFound.setBankCode(request.getBankCode());

        bankRepository.saveAndFlush(bankFound);

        return BankResponse.builder()
                .bankName(bankFound.getName())
                .bankCode(bankFound.getBankCode())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(String id, Boolean status) {
        findByIdOrThrowNotFound(id);

        bankRepository.updateStatus(id, status);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        findByIdOrThrowNotFound(id);

        bankRepository.updateStatus(id, false);
    }

    private Bank findByIdOrThrowNotFound(String request) {
        return bankRepository.findById(request)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }
}
