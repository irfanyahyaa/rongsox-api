package com.enigma.bank_sampah.controller;

import com.enigma.bank_sampah.constant.APIUrl;
import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.dto.request.DepositRequest;
import com.enigma.bank_sampah.dto.request.SearchTransactionRequest;
import com.enigma.bank_sampah.dto.request.UpdateWithdrawalRequest;
import com.enigma.bank_sampah.dto.request.WithdrawalRequest;
import com.enigma.bank_sampah.dto.response.CommonResponse;
import com.enigma.bank_sampah.dto.response.PagingResponse;
import com.enigma.bank_sampah.dto.response.StuffResponse;
import com.enigma.bank_sampah.dto.response.TransactionResponse;
import com.enigma.bank_sampah.service.TransactionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.TRANSACTION_API)
public class TransactionController {
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    @PostMapping(
            path = "/deposit",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> createDeposit(
            @RequestBody DepositRequest request
    ) {
        TransactionResponse transaction = transactionService.createDeposit(request);

        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(transaction)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(
            path = "/withdrawal",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> createWithdrawal(
            @RequestBody WithdrawalRequest request
    ) {
        TransactionResponse transaction = transactionService.createWithdrawal(request);

        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(transaction)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> getAllTransactions(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "transactionDate") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "transactionType", required = false) String transactionType,
            @RequestParam(name = "status", required = false) String status
    ) {
        SearchTransactionRequest request = SearchTransactionRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .transactionType(transactionType)
                .status(status)
                .build();

        Page<TransactionResponse> transactions = transactionService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(transactions.getTotalPages())
                .totalElement(transactions.getTotalElements())
                .page(transactions.getPageable().getPageNumber() + 1)
                .size(transactions.getPageable().getPageSize())
                .hasNext(transactions.hasNext())
                .hasPrevious(transactions.hasPrevious())
                .build();

        CommonResponse<List<TransactionResponse>> commonResponse = CommonResponse.<List<TransactionResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(transactions.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(commonResponse);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping(
            path = "/deposit/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> updateTransactionStatusById(
            @PathVariable String id,
            @RequestParam String status
    ) {
        transactionService.updateStatusById(id, status);

        CommonResponse<StuffResponse> response = CommonResponse.<StuffResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping(
            path = "/withdrawal",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> updateWithdrawalStatusById(
            @RequestPart(name = "withdrawal") String jsonMenu,
            @RequestPart(name = "image", required = false) MultipartFile image
    ) {
        CommonResponse.CommonResponseBuilder<TransactionResponse> responseBuilder = CommonResponse.builder();

        try {
            UpdateWithdrawalRequest request = objectMapper.readValue(jsonMenu, new TypeReference<>() {
            });
            request.setImage(image);
            TransactionResponse menu = transactionService.updateStatusWithdrawal(request);

            CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                    .data(menu)
                    .build();

            return ResponseEntity
                    .ok(response);
        } catch (Exception exception) {
            responseBuilder.message(ResponseMessage.ERROR_INTERNAL_SERVER);
            responseBuilder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBuilder.build());
        }
    }
}
