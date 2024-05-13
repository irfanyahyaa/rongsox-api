package com.enigma.bank_sampah.controller;

import com.enigma.bank_sampah.constant.APIUrl;
import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.dto.request.BankAccountRequest;
import com.enigma.bank_sampah.dto.response.BankAccountResponse;
import com.enigma.bank_sampah.dto.response.CommonResponse;
import com.enigma.bank_sampah.service.BankAccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.BANK_ACCOUNT_API)
@Slf4j
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'CUSTOMER')")
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> createBankAccount(
            @RequestBody BankAccountRequest request
    ) {
        BankAccountResponse bankAccountResponse = bankAccountService.create(request);

        CommonResponse<BankAccountResponse> response = CommonResponse.<BankAccountResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(bankAccountResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'CUSTOMER')")
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<String>> deleteBankAccountById(
            @PathVariable(name = "id") String id
    ) {
        bankAccountService.deleteById(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();

        return ResponseEntity.ok(response);
    }
}
