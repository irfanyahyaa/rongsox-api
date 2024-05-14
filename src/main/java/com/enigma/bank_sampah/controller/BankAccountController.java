package com.enigma.bank_sampah.controller;

import com.enigma.bank_sampah.constant.APIUrl;
import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.dto.request.BankAccountRequest;
import com.enigma.bank_sampah.dto.request.SearchBankAccountRequest;
import com.enigma.bank_sampah.dto.request.UpdateBankAccountRequest;
import com.enigma.bank_sampah.dto.response.BankAccountResponse;
import com.enigma.bank_sampah.dto.response.CommonResponse;
import com.enigma.bank_sampah.dto.response.PagingResponse;
import com.enigma.bank_sampah.service.BankAccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> getAllBankAccounts(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "accountNumber") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "accountNumber", required = false) String accountNumber,
            @RequestParam(name = "status", required = false) Boolean status

    ) {
        SearchBankAccountRequest request = SearchBankAccountRequest.builder()
                .page(Math.max(page - 1, 0))
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .accountNumber(accountNumber)
                .status(status)
                .build();

        Page<BankAccountResponse> bankAccountPages = bankAccountService.getAll(request);

        PagingResponse paging = PagingResponse.builder()
                .totalPages(bankAccountPages.getTotalPages())
                .totalElement(bankAccountPages.getTotalElements())
                .page(page)
                .size(size)
                .hasPrevious(bankAccountPages.hasPrevious())
                .hasNext(bankAccountPages.hasNext())
                .build();

        CommonResponse<List<BankAccountResponse>> response = CommonResponse.<List<BankAccountResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .paging(paging)
                .data(bankAccountPages.getContent())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> getBankAccountById(
            @PathVariable String id
    ) {
        BankAccountResponse bankResponse = bankAccountService.getByIdDTO(id);

        CommonResponse<BankAccountResponse> response = CommonResponse.<BankAccountResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(bankResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> updateBankAccount(
            @RequestBody UpdateBankAccountRequest request
    ) {
        BankAccountResponse update = bankAccountService.update(request);

        CommonResponse<BankAccountResponse> response = CommonResponse.<BankAccountResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(update)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'CUSTOMER')")
    @PutMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> UpdateBankAccountStatusById(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "status") Boolean status
    ) {
        bankAccountService.updateStatus(id, status);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'CUSTOMER')")
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> deleteBankAccountById(
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
