package com.enigma.bank_sampah.controller;

import com.enigma.bank_sampah.constant.APIUrl;
import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.dto.request.BankRequest;
import com.enigma.bank_sampah.dto.request.SearchBankRequest;
import com.enigma.bank_sampah.dto.request.UpdateBankRequest;
import com.enigma.bank_sampah.dto.response.BankResponse;
import com.enigma.bank_sampah.dto.response.CommonResponse;
import com.enigma.bank_sampah.dto.response.PagingResponse;
import com.enigma.bank_sampah.service.BankService;
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
@RequestMapping(path = APIUrl.BANK_API)
@Slf4j
public class BankController {
    private final BankService bankService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> createBank(
            @RequestBody BankRequest request
    ) {
        BankResponse bankResponse = bankService.create(request);

        CommonResponse<BankResponse> response = CommonResponse.<BankResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(bankResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<List<BankResponse>>> getAllBanks(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "bankCode") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "q", required = false) String query

    ) {
        SearchBankRequest request = SearchBankRequest.builder()
                .page(Math.max(page - 1, 0))
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .query(query)
                .build();

        Page<BankResponse> responsePage = bankService.getAll(request);

        PagingResponse paging = PagingResponse.builder()
                .totalPages(responsePage.getTotalPages())
                .totalElement(responsePage.getTotalElements())
                .page(page)
                .size(size)
                .hasPrevious(responsePage.hasPrevious())
                .hasNext(responsePage.hasNext())
                .build();

        CommonResponse<List<BankResponse>> response = CommonResponse.<List<BankResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .paging(paging)
                .data(responsePage.getContent())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> getBankById(
            @PathVariable String id
    ) {
        BankResponse bankResponse = bankService.getByIdDTO(id);

        CommonResponse<BankResponse> response = CommonResponse.<BankResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(bankResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<?> updateBank(
            @RequestBody UpdateBankRequest request
    ) {
        BankResponse update = bankService.update(request);

        CommonResponse<BankResponse> response = CommonResponse.<BankResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(update)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<String>> deleteBankById(
            @PathVariable String id
    ) {
        bankService.deleteById(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();

        return ResponseEntity.ok(response);
    }
}
