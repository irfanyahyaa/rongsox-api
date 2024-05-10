package com.enigma.bank_sampah.controller;

import com.enigma.bank_sampah.constant.APIUrl;
import com.enigma.bank_sampah.dto.request.SearchAdminRequest;
import com.enigma.bank_sampah.dto.response.AdminResponse;
import com.enigma.bank_sampah.dto.response.CommonResponse;
import com.enigma.bank_sampah.dto.response.PagingResponse;
import com.enigma.bank_sampah.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIUrl.ADMIN_API)
public class AdminController {
    private final AdminService adminService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping
    public ResponseEntity<CommonResponse<List<AdminResponse>>> getAllUsers(
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
//                .query(name)
                .build();

        Page<AdminResponse> admins = adminService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(admins.getTotalPages())
                .totalElement(admins.getTotalElements())
                .page(admins.getPageable().getPageNumber() + 1)
                .size(admins.getPageable().getPageSize())
                .hasNext(admins.hasNext())
                .hasPrevious(admins.hasPrevious())
                .build();

        CommonResponse<List<AdminResponse>> commonResponse = CommonResponse.<List<AdminResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("users fetched successfully")
                .data(admins.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity
                .ok(commonResponse);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<String>> deleteUser(
            @PathVariable(name = "id") String id
    ) {
        adminService.deleteById(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("admin deleted successfully")
                .build();
        return ResponseEntity
                .ok(response);

//        AdminResponse admin = adminService.deleteById(id);
//
//        CommonResponse<UserResponse> commonResponse = CommonResponse.<UserResponse>builder()
//                .statusCode(HttpStatus.OK.value())
//                .message("user deleted successfully")
//                .data(user)
//                .build();
//
//        return ResponseEntity
//                .ok(commonResponse);
    }
}
