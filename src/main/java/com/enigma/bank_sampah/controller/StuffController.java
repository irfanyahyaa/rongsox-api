package com.enigma.bank_sampah.controller;

import com.enigma.bank_sampah.constant.APIUrl;
import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.dto.request.SearchStuffRequest;
import com.enigma.bank_sampah.dto.request.StuffRequest;
import com.enigma.bank_sampah.dto.request.UpdateStuffRequest;
import com.enigma.bank_sampah.dto.response.CommonResponse;
import com.enigma.bank_sampah.dto.response.PagingResponse;
import com.enigma.bank_sampah.dto.response.StuffResponse;
import com.enigma.bank_sampah.service.StuffService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping(path = APIUrl.STUFF_API)
@Slf4j
public class StuffController {
    private final StuffService stuffService;
    private final ObjectMapper objectMapper;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> createStuff(
            @RequestPart(name = "stuff") String jsonStuff,
            @RequestPart(name = "image", required = false) MultipartFile image
    ) {
        CommonResponse.CommonResponseBuilder<StuffResponse> responseBuilder = CommonResponse.builder();

        try {
            StuffRequest request = objectMapper.readValue(jsonStuff, new TypeReference<>() {
            });
            request.setImage(image);
            StuffResponse stuff = stuffService.create(request);

            responseBuilder.statusCode(HttpStatus.CREATED.value());
            responseBuilder.message(ResponseMessage.SUCCESS_SAVE_DATA);
            responseBuilder.data(stuff);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(responseBuilder.build());
        } catch (Exception exception) {
            responseBuilder.message(ResponseMessage.ERROR_INTERNAL_SERVER);
            responseBuilder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBuilder.build());
        }
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'CUSTOMER')")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> getAllStuffs(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "stuffName") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "stuffName", required = false) String stuffName,
            @RequestParam(name = "status", required = false) Boolean status
    ) {
        SearchStuffRequest request = SearchStuffRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .stuffName(stuffName)
                .status(status)
                .build();

        Page<StuffResponse> stuffPages = stuffService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(stuffPages.getTotalPages())
                .totalElement(stuffPages.getTotalElements())
                .page(stuffPages.getPageable().getPageNumber() + 1)
                .size(stuffPages.getPageable().getPageSize())
                .hasNext(stuffPages.hasNext())
                .hasPrevious(stuffPages.hasPrevious())
                .build();

        CommonResponse<List<StuffResponse>> response = CommonResponse.<List<StuffResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(stuffPages.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'CUSTOMER')")
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> getStuffById(
            @PathVariable String id
    ) {
        StuffResponse stuffResponse = stuffService.getByIdDTO(id);

        CommonResponse<StuffResponse> response = CommonResponse.<StuffResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(stuffResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> updateStuff(
            @RequestPart(name = "stuff") String jsonStuff,
            @RequestPart(name = "image", required = false) MultipartFile image
    ) {
        CommonResponse.CommonResponseBuilder<StuffResponse> responseBuilder = CommonResponse.builder();

        try {
            UpdateStuffRequest request = objectMapper.readValue(jsonStuff, new TypeReference<>() {});
            request.setImage(image);
            StuffResponse stuff = stuffService.update(request);

            CommonResponse<StuffResponse> response = CommonResponse.<StuffResponse>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                    .data(stuff)
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

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> updateStuffStatusById(
            @PathVariable String id,
            @RequestParam boolean status
    ) {
        stuffService.updateStatusById(id, status);

        CommonResponse<StuffResponse> response = CommonResponse.<StuffResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CommonResponse<?>> deleteStuffById(
            @PathVariable String id
    ) {
        stuffService.deleteById(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();

        return ResponseEntity.ok(response);
    }
}
