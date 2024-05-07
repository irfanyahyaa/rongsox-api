package com.enigma.bank_sampah.controller;

import com.enigma.bank_sampah.constant.APIUrl;
import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.dto.request.*;
import com.enigma.bank_sampah.dto.response.CommonResponse;
import com.enigma.bank_sampah.dto.response.LoginResponse;
import com.enigma.bank_sampah.dto.response.RegisterResponse;
import com.enigma.bank_sampah.dto.response.ValidationOtpResponse;
import com.enigma.bank_sampah.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = APIUrl.AUTH_API)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(path = "/register/customer",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> registerUser(@RequestBody CustomerRequest request) {
        RegisterResponse register = authService.registerCustomer(request);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(register)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PostMapping(path = "/register/customerByAdmin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> registerUserByAdmin(@RequestBody CustomerRequest request) {
        RegisterResponse register = authService.registerCustomerByAdmin(request);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(register)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(path = "/resend-otp",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> resendOtp(@RequestBody ResendOtpRequest request) {
        authService.resendOtp(request);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_SEND_OTP)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/forgot-password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_SEND_OTP)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/set-new-password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )public ResponseEntity<CommonResponse<?>> setNewPassword(@RequestBody SetNewPasswordRequest request) {
        authService.setNewPassword(request);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_SEND_OTP)
                .build();
        return ResponseEntity.ok(response);
    }


    @PostMapping(path = "/validation-otp",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> registerUser(@RequestBody ValidationOtpRequest request) {
        ValidationOtpResponse validationOtpResponse = authService.validateOtp(request);
        CommonResponse<ValidationOtpResponse> response = CommonResponse.<ValidationOtpResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_VALIDATE_OTP)
                .data(validationOtpResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @PostMapping(path = "/register/admin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<CommonResponse<?>> registerAdmin(@RequestBody AdminRequest request) {
        RegisterResponse register = authService.registerAdmin(request);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(register)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> login(@RequestBody AuthRequest request) {
        LoginResponse loginResponse = authService.login(request);
        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_LOGIN)
                .data(loginResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "validate-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validateToken() {
        boolean valid = authService.validateToken();
        if (valid) {
            CommonResponse<String> response = CommonResponse.<String>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message(ResponseMessage.SUCCESS_GET_DATA)
                    .build();
            return ResponseEntity.ok(response);
        } else {
            CommonResponse<String> response = CommonResponse.<String>builder()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .message(ResponseMessage.ERROR_INVALID_JWT)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}
