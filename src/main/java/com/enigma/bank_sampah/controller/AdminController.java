package com.enigma.bank_sampah.controller;

import com.enigma.bank_sampah.constant.APIUrl;
import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.dto.request.SearchAdminRequest;
import com.enigma.bank_sampah.dto.request.UpdateAdminRequest;
import com.enigma.bank_sampah.dto.response.AdminResponse;
import com.enigma.bank_sampah.dto.response.CommonResponse;
import com.enigma.bank_sampah.dto.response.PagingResponse;
import com.enigma.bank_sampah.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIUrl.ADMIN_API)
public class AdminController {
    private final AdminService adminService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> getAllAdmins(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "status", required = false) Boolean status
    ) {
        SearchAdminRequest request = SearchAdminRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .name(name)
                .status(status)
                .build();

        Page<AdminResponse> adminPages = adminService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(adminPages.getTotalPages())
                .totalElement(adminPages.getTotalElements())
                .page(adminPages.getPageable().getPageNumber() + 1)
                .size(adminPages.getPageable().getPageSize())
                .hasNext(adminPages.hasNext())
                .hasPrevious(adminPages.hasPrevious())
                .build();

        CommonResponse<List<AdminResponse>> commonResponse = CommonResponse.<List<AdminResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(adminPages.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(commonResponse);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(path = "/{id}")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> getAdminById(
            @PathVariable(name = "id") String id
    ) {
        AdminResponse menu = adminService.getByIdDTO(id);

        CommonResponse<AdminResponse> response = CommonResponse.<AdminResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(menu)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> updateAdmin(
            @RequestBody UpdateAdminRequest request
    ) {
        AdminResponse admin = adminService.update(request);

        CommonResponse<AdminResponse> response = CommonResponse.<AdminResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(admin)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PutMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> updateAdminStatusById(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "status") Boolean status
    ) {
        adminService.updateStatusById(id, status);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> deleteAdminById(
            @PathVariable(name = "id") String id
    ) {
        adminService.deleteById(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();

        return ResponseEntity.ok(response);
    }
}
