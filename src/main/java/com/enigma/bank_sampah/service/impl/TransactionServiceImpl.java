package com.enigma.bank_sampah.service.impl;

import com.enigma.bank_sampah.constant.APIUrl;
import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.dto.request.DepositRequest;
import com.enigma.bank_sampah.dto.request.SearchTransactionRequest;
import com.enigma.bank_sampah.dto.request.UpdateWithdrawalRequest;
import com.enigma.bank_sampah.dto.request.WithdrawalRequest;
import com.enigma.bank_sampah.dto.response.ImageResponse;
import com.enigma.bank_sampah.dto.response.TransactionDetailResponse;
import com.enigma.bank_sampah.dto.response.TransactionResponse;
import com.enigma.bank_sampah.entity.*;
import com.enigma.bank_sampah.repository.TransactionRepository;
import com.enigma.bank_sampah.service.*;
import com.enigma.bank_sampah.specification.TransactionSpecification;
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

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionDetailService transactionDetailService;
    private final AdminService adminService;
    private final BankAccountService bankAccountService;
    private final CustomerService customerService;
    private final StuffService stuffService;
    private final ImageService imageService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse createDeposit(DepositRequest request) {
        Customer customer = customerService.getByIdEntity(request.getCustomerId());
        Admin admin = adminService.getByIdEntity(request.getAdminId());

        Transaction transaction = Transaction.builder()
                .transactionDate(new Timestamp(System.currentTimeMillis()))
                .transactionType("Deposit")
                .admin(admin)
                .customer(customer)
                .amount(0L)
                .status("Pending")
                .build();
        transactionRepository.saveAndFlush(transaction);

        List<TransactionDetail> transactionDetails = request.getTransactionDetails().stream()
                .map(detailRequest -> {
                    Stuff stuff = stuffService.getByIdEntity(detailRequest.getStuffId());

                    transaction.setAmount((long) (transaction.getAmount() + stuff.getBuyingPrice() * detailRequest.getWeight()));

                    return TransactionDetail.builder()
                            .transaction(transaction)
                            .stuff(stuff)
                            .weight(detailRequest.getWeight())
                            .buyingPrice(stuff.getBuyingPrice())
                            .build();
                }).toList();
        transactionDetailService.createBulk(transactionDetails);
        transaction.setTransactionDetails(transactionDetails);
        customerService.updateBalanceById(request.getCustomerId(), transaction.getAmount());

        List<TransactionDetailResponse> transactionDetailResponses = transactionDetails.stream().map(detail ->
                TransactionDetailResponse.builder()
                        .id(detail.getId())
                        .stuffName(detail.getStuff().getStuffName())
                        .weight(detail.getWeight())
                        .buyingPrice(detail.getBuyingPrice())
                        .build()).toList();

        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionDate(transaction.getTransactionDate())
                .transactionType(transaction.getTransactionType())
                .customerId(customer.getId())
                .customerName(customer.getName())
                .customerPhoneNumber(customer.getPhoneNumber())
                .customerAddress(customer.getAddress())
                .adminId(admin.getId())
                .adminName(admin.getName())
                .adminPhoneNumber(admin.getPhoneNumber())
                .adminAddress(admin.getAddress())
                .amount(transaction.getAmount())
                .transactionDetails(transactionDetailResponses)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse createWithdrawal(WithdrawalRequest request) {
        Customer customer = customerService.getByIdEntity(request.getCustomerId());
        Admin admin = adminService.getByIdEntity(request.getAdminId());

        Transaction transaction = Transaction.builder()
                .transactionDate(new Timestamp(System.currentTimeMillis()))
                .transactionType("Withdrawal")
                .admin(admin)
                .customer(customer)
                .bankAccount(bankAccountService.getByIdEntity(request.getBankAccountId()))
                .paymentMethod("Transfer")
                .amount(request.getAmount())
                .status("Pending")
                .build();
        transactionRepository.saveAndFlush(transaction);
        customerService.updateBalanceById(request.getCustomerId(), -request.getAmount());

        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionDate(transaction.getTransactionDate())
                .transactionType(transaction.getTransactionType())
                .adminId(admin.getId())
                .adminName(admin.getName())
                .adminPhoneNumber(admin.getPhoneNumber())
                .adminAddress(admin.getAddress())
                .customerId(transaction.getCustomer().getId())
                .customerName(transaction.getCustomer().getName())
                .customerPhoneNumber(transaction.getCustomer().getPhoneNumber())
                .customerAddress(transaction.getCustomer().getAddress())
                .bankName(transaction.getBankAccount().getBank().getName())
                .bankCode(transaction.getBankAccount().getBank().getBankCode())
                .accountNumber(transaction.getBankAccount().getAccountNumber())
                .paymentMethod(transaction.getPaymentMethod())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TransactionResponse> getAll(SearchTransactionRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);
        Specification<Transaction> specification = TransactionSpecification.getSpecification(request);

        Page<Transaction> transactionPage = transactionRepository.findAll(specification, pageable);

        return transactionPage.map(transaction ->
                {
                    if (transaction.getTransactionType().equalsIgnoreCase("Deposit")) {
                        return TransactionResponse.builder()
                                .id(transaction.getId())
                                .transactionDate(transaction.getTransactionDate())
                                .transactionType(transaction.getTransactionType())
                                .transactionDetails(transaction.getTransactionDetails().stream().map(detail ->
                                        TransactionDetailResponse.builder()
                                                .id(detail.getId())
                                                .stuffName(detail.getStuff().getStuffName())
                                                .weight(detail.getWeight())
                                                .buyingPrice(detail.getBuyingPrice())
                                                .build()).toList())
                                .adminId(transaction.getAdmin().getId())
                                .adminName(transaction.getAdmin().getName())
                                .adminPhoneNumber(transaction.getAdmin().getPhoneNumber())
                                .adminAddress(transaction.getAdmin().getAddress())
                                .customerId(transaction.getCustomer().getId())
                                .customerName(transaction.getCustomer().getName())
                                .customerPhoneNumber(transaction.getCustomer().getPhoneNumber())
                                .customerAddress(transaction.getCustomer().getAddress())
                                .amount(transaction.getAmount())
                                .status(transaction.getStatus())
                                .build();
                    } else if (transaction.getTransactionType().equalsIgnoreCase("Withdrawal") && transaction.getTransferReceipt() != null) {
                        return TransactionResponse.builder()
                                .id(transaction.getId())
                                .transactionDate(transaction.getTransactionDate())
                                .transactionType(transaction.getTransactionType())
                                .adminId(transaction.getAdmin().getId())
                                .adminName(transaction.getAdmin().getName())
                                .adminPhoneNumber(transaction.getAdmin().getPhoneNumber())
                                .adminAddress(transaction.getAdmin().getAddress())
                                .customerId(transaction.getCustomer().getId())
                                .customerName(transaction.getCustomer().getName())
                                .customerPhoneNumber(transaction.getCustomer().getPhoneNumber())
                                .customerAddress(transaction.getCustomer().getAddress())
                                .bankName(transaction.getBankAccount().getBank().getName())
                                .bankCode(transaction.getBankAccount().getBank().getBankCode())
                                .accountNumber(transaction.getBankAccount().getAccountNumber())
                                .paymentMethod(transaction.getPaymentMethod())
                                .amount(transaction.getAmount())
                                .transferReceipt(ImageResponse.builder()
                                        .url(APIUrl.TRANSACTION_IMAGE__API + "/" + transaction.getTransferReceipt().getId())
                                        .name(transaction.getTransferReceipt().getName())
                                        .build())
                                .status(transaction.getStatus())
                                .build();
                    } else {
                        return TransactionResponse.builder()
                                .id(transaction.getId())
                                .transactionDate(transaction.getTransactionDate())
                                .transactionType(transaction.getTransactionType())
                                .adminId(transaction.getAdmin().getId())
                                .adminName(transaction.getAdmin().getName())
                                .adminPhoneNumber(transaction.getAdmin().getPhoneNumber())
                                .adminAddress(transaction.getAdmin().getAddress())
                                .customerId(transaction.getCustomer().getId())
                                .customerName(transaction.getCustomer().getName())
                                .customerPhoneNumber(transaction.getCustomer().getPhoneNumber())
                                .customerAddress(transaction.getCustomer().getAddress())
                                .bankName(transaction.getBankAccount().getBank().getName())
                                .bankCode(transaction.getBankAccount().getBank().getBankCode())
                                .accountNumber(transaction.getBankAccount().getAccountNumber())
                                .paymentMethod(transaction.getPaymentMethod())
                                .amount(transaction.getAmount())
                                .status(transaction.getStatus())
                                .build();
                    }
                }
        );
    }

    @Override
    public TransactionResponse updateStatusWithdrawal(UpdateWithdrawalRequest request) {
        Transaction transaction = findByIdOrThrowNotFound(request.getId());

        if (request.getImage() != null && transaction.getTransferReceipt() != null) {
            Image image = imageService.create(request.getImage());
            String deletedImage = transaction.getTransferReceipt().getId();

            transaction.setTransferReceipt(image);
            imageService.deleteById(deletedImage);
        } else {
            Image image = imageService.create(request.getImage());
            transaction.setTransferReceipt(image);
        }
        transaction.setStatus(request.getStatus());
        transactionRepository.saveAndFlush(transaction);

        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionDate(transaction.getTransactionDate())
                .transactionType(transaction.getTransactionType())
                .adminId(transaction.getAdmin().getId())
                .adminName(transaction.getAdmin().getName())
                .adminPhoneNumber(transaction.getAdmin().getPhoneNumber())
                .adminAddress(transaction.getAdmin().getAddress())
                .customerId(transaction.getCustomer().getId())
                .customerName(transaction.getCustomer().getName())
                .customerPhoneNumber(transaction.getCustomer().getPhoneNumber())
                .customerAddress(transaction.getCustomer().getAddress())
                .bankName(transaction.getBankAccount().getBank().getName())
                .bankCode(transaction.getBankAccount().getBank().getBankCode())
                .accountNumber(transaction.getBankAccount().getAccountNumber())
                .paymentMethod(transaction.getPaymentMethod())
                .amount(transaction.getAmount())
                .transferReceipt(ImageResponse.builder()
                        .url(APIUrl.TRANSACTION_IMAGE__API + "/" + transaction.getTransferReceipt().getId())
                        .name(transaction.getTransferReceipt().getName())
                        .build())
                .status(transaction.getStatus())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatusById(String id, String status) {
        Transaction transaction = findByIdOrThrowNotFound(id);
        transaction.setStatus(status);

        transactionRepository.saveAndFlush(transaction);
    }

    private Transaction findByIdOrThrowNotFound(String request) {
        return transactionRepository.findById(request)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }
}
