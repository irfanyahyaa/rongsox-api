package com.enigma.bank_sampah.service;

import com.enigma.bank_sampah.dto.request.*;
import com.enigma.bank_sampah.dto.response.LoginResponse;
import com.enigma.bank_sampah.dto.response.RegisterResponse;
import com.enigma.bank_sampah.dto.response.ValidationOtpResponse;

public interface AuthService {
    RegisterResponse registerCustomer(CustomerRequest request);
    RegisterResponse registerCustomerByAdmin(CustomerRequest request);
    RegisterResponse registerAdmin(AdminRequest request);
    LoginResponse login(AuthRequest request);
    boolean validateToken();
    ValidationOtpResponse validateOtp(ValidationOtpRequest request);
    void resendOtp(ResendOtpRequest request);
    void forgotPassword(ForgotPasswordRequest request);

    void setNewPassword(SetNewPasswordRequest request);
}
