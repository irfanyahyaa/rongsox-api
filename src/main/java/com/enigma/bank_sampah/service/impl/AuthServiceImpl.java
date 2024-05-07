package com.enigma.bank_sampah.service.impl;

import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.constant.UserRole;
import com.enigma.bank_sampah.dto.request.*;
import com.enigma.bank_sampah.dto.response.LoginResponse;
import com.enigma.bank_sampah.dto.response.RegisterResponse;
import com.enigma.bank_sampah.dto.response.ValidationOtpResponse;
import com.enigma.bank_sampah.entity.*;
import com.enigma.bank_sampah.repository.UserAccountRepository;
import com.enigma.bank_sampah.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserAccountRepository userAccountRepository;
    private final RoleService roleService;
    private final CustomerService customerService;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final EmailService emailService;

    @Value("${bank_sampah.username.superadmin}")
    private String superAdminUsername;
    @Value("${bank_sampah.password.superadmin}")
    private String superAdminPassword;
    @Value("${bank_sampah.email.superadmin}")
    private String superAdminEmail;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initSuperAdmin() {
        Optional<UserAccount> currentUser = userAccountRepository.findByUsername(superAdminUsername);
        if (currentUser.isPresent()) return;

        Role superAdmin = roleService.getOrSave(UserRole.ROLE_SUPER_ADMIN);
        Role adminRole = roleService.getOrSave(UserRole.ROLE_ADMIN);
        Role customer = roleService.getOrSave(UserRole.ROLE_CUSTOMER);

        UserAccount account = UserAccount.builder()
                .username(superAdminUsername)
                .password(passwordEncoder.encode(superAdminPassword))
                .email(superAdminEmail)
                .role(List.of(superAdmin, adminRole, customer))
                .isEnable(true)
                .build();
        userAccountRepository.saveAndFlush(account);

        Admin admin = Admin.builder()
                .status(true)
                .userAccount(account)
                .build();
        adminService.create(admin);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerCustomer(CustomerRequest request) throws DataIntegrityViolationException {
        Optional<UserAccount> byUsername = userAccountRepository.findByUsername(request.getUsername());
        if (byUsername.isPresent()) {
            throw new DataIntegrityViolationException("User already exists");
        }

        Optional<UserAccount> byEmail = userAccountRepository.findByEmail(request.getEmail());
        if (byEmail.isPresent()) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        Role role = roleService.getOrSave(UserRole.ROLE_CUSTOMER);
        String hashPassword = passwordEncoder.encode(request.getPassword());

        UserAccount account = UserAccount.builder()
                .username(request.getUsername())
                .password(hashPassword)
                .email(request.getEmail())
                .role(List.of(role))
                .isEnable(false)
                .build();

        userAccountRepository.saveAndFlush(account);

        Customer customer = Customer.builder()
                .name(request.getName())
                .phoneNumber(request.getMobilePhoneNo())
                .address(request.getAddress())
                .birthDate(request.getBirthDate())
                .ktpNumber(request.getKtpNumber())
                .status(true)
                .userAccount(account)
                .build();
        customerService.create(customer);

        List<String> roles = account.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        Token token = tokenService.createToken();
        token.setTokenType("registration");
        token.setCustomer(customer);

        sendVerificationEmail(account.getEmail(),token.getToken());

        tokenService.saveToken(token);

        return RegisterResponse.builder()
                .username(account.getUsername())
                .email(account.getEmail())
                .roles(roles)
                .type(token.getTokenType())
                .message(ResponseMessage.SUCCESS_REGISTER)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerAdmin(AdminRequest request) {
        Role roleAdmin = roleService.getOrSave(UserRole.ROLE_ADMIN);
        Role roleCustomer = roleService.getOrSave(UserRole.ROLE_CUSTOMER);

        String hashPassword = passwordEncoder.encode(request.getPassword());

        UserAccount account = UserAccount.builder()
                .username(request.getUsername())
                .password(hashPassword)
                .email(request.getEmail())
                .role(List.of(roleAdmin, roleCustomer))
                .isEnable(true)
                .build();

        userAccountRepository.saveAndFlush(account);

        Admin admin = Admin.builder()
                .name(request.getName())
                .address(request.getAddress())
                .position(request.getPosition())
                .status(true)
                .userAccount(account)
                .build();
        adminService.create(admin);

        Customer customer = Customer.builder()
                .status(true)
                .userAccount(account)
                .build();
        customerService.create(customer);

        List<String> roles = account.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return RegisterResponse.builder()
                .username(account.getUsername())
                .roles(roles)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(AuthRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserAccount userAccount = (UserAccount) authenticate.getPrincipal();
        String token = jwtService.generateToken(userAccount);

        LoginResponse response = LoginResponse.builder()
                .userAccountId(userAccount.getId())
                .username(userAccount.getUsername())
                .roles(userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .token(token)
                .build();

        if( customerService.getByIdUserAccount(userAccount.getId()) != null ){
            response.setCustomerId(customerService.getByIdUserAccount(userAccount.getId()).getId());
        }

        if( adminService.getByUserAccountId(userAccount.getId()) != null ){
            response.setAdminId(adminService.getByUserAccountId(userAccount.getId()).getId());
        }

        return response;

    }

    @Override
    public boolean validateToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = userAccountRepository.findByUsername(authentication.getPrincipal().toString())
                .orElse(null);
        return userAccount != null;
    }

    @Override
    public ValidationOtpResponse validateOtp(ValidationOtpRequest request) {
        String otp = request.getOtp();
        String email = request.getEmail();
        String type = request.getTypeOtp();

        Token token = tokenService.findByToken(otp);
        if (token == null) {
            throw new RuntimeException("Invalid OTP");
        }

        UserAccount userAccount = token.getCustomer().getUserAccount();
        if (!userAccount.getEmail().equals(email)) {
            throw new RuntimeException("Invalid email");
        }

        Date now = new Date();
        if (token.getExpiredAt().before(now)) {
            throw new RuntimeException("OTP has expired");
        }

        if(type.equalsIgnoreCase("registration")){
            userAccount.setIsEnable(true);
            tokenService.removeToken(token);
        }

        return ValidationOtpResponse.builder()
                .isValid(true)
                .typeOtp(type)
                .otp(otp)
                .email(email)
                .message(ResponseMessage.SUCCESS_VALIDATE_OTP)
                .build();
    }

    @Override
    public void resendOtp(ResendOtpRequest request) {
        String email = request.getEmail();
        String typeOtp = request.getTypeOtp();

        Optional<UserAccount> byEmail = userAccountRepository.findByEmail(email);
        if(byEmail.isEmpty()){
            throw new RuntimeException("Email not found");
        }

        UserAccount userAccount = byEmail.orElse(null);
        if(userAccount.getIsEnable()){
            throw new RuntimeException("User account already activated, please login.");
        }

        Customer byIdUserAccount = customerService.getByIdUserAccount(userAccount.getId());

        tokenService.removeTokenByCustomerAndType(byIdUserAccount, typeOtp);

        Token token = tokenService.createToken();
        token.setTokenType("registration");
        token.setCustomer(byIdUserAccount);

        sendVerificationEmail(userAccount.getEmail(),token.getToken());

        tokenService.saveToken(token);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        String email = request.getEmail();
        Optional<UserAccount> byEmail = userAccountRepository.findByEmail(email);

        if(byEmail.isEmpty()){
            throw new RuntimeException(ResponseMessage.ERROR_NOT_FOUND);
        }
        UserAccount userAccount = byEmail.orElse(null);

        Customer byIdUserAccount = customerService.getByIdUserAccount(userAccount.getId());

        Token token = tokenService.createToken();
        token.setTokenType("forgot-password");
        token.setCustomer(byIdUserAccount);

        tokenService.saveToken(token);

        sendVerificationEmail(email,token.getToken());
    }

    @Override
    public void setNewPassword(SetNewPasswordRequest request) {
        String password = request.getPassword();
        String otp = request.getOtp();
        String email = request.getEmail();
        String typeOtp = request.getTypeOtp();

        Optional<UserAccount> byEmail = userAccountRepository.findByEmail(email);
        if(byEmail.isEmpty()) throw new RuntimeException(ResponseMessage.ERROR_NOT_FOUND);

        UserAccount userAccount = byEmail.get();
        Token token = tokenService.findByToken(otp);
        if (token == null || !token.getCustomer().getUserAccount().getId().equals(userAccount.getId())) {
            throw new RuntimeException("Invalid OTP");
        }

        userAccount.setPassword(passwordEncoder.encode(password));
        userAccountRepository.save(userAccount);

        tokenService.removeToken(token);
    }

    private void sendVerificationEmail(String email,String otp){
        String subject = "Email verification";
        String body ="your verification otp is: "+ otp;
        emailService.sendEmail(email,subject,body);
    }

}