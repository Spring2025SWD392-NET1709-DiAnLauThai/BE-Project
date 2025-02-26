package com.be.back_end.service.AccountService;


import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.request.CreateAccountRequest;
import com.be.back_end.dto.request.RegisterRequest;
import com.be.back_end.dto.response.AccountCreationResponse;
import com.be.back_end.dto.response.JwtResponse;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.dto.response.TokenValidateDTO;

import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import com.be.back_end.model.Account;
import com.be.back_end.repository.AccountRepository;

import com.be.back_end.security.jwt.JwtUtils;
import com.be.back_end.service.EmailService.IEmailService;
import com.be.back_end.utils.AccountUtils;
import com.be.back_end.utils.PasswordUtils;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class AccountService implements IAccountService{
    /*private final IGenericService<Account, UUID> genericService;*/
    private final AccountUtils accountUtils;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final JwtUtils jwtUtils;
    private final IEmailService emailService;
    private final IGoogleService googleService;
    public AccountService(AccountUtils accountUtils, PasswordEncoder passwordEncoder, AccountRepository accountRepository, JwtUtils jwtUtils, IEmailService emailService, IGoogleService googleService) {
        this.accountUtils = accountUtils;
        this.passwordEncoder = passwordEncoder;

        this.accountRepository = accountRepository;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
        this.googleService = googleService;
    }
    private AccountDTO mapToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(UUID.fromString(account.getId()));
        dto.setEmail(account.getEmail());
        dto.setName(account.getName());
        dto.setAddress(account.getAddress());
        dto.setPhone(account.getPhone());
        dto.setRole(account.getRole());
        dto.setStatus(account.getStatus().toString());
        dto.setDateOfBirth(account.getDateOfBirth());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());
        return dto;
    }


    @Override
    public boolean validateToken() {
        String token= accountUtils.getCurrentJwtToken();
        if (jwtUtils.validateJwtToken(token)) {
            return true;
        }
        return false;
    }

    @Override
    public JwtResponse refreshAccessToken(String refreshToken) {
        try {
            if (!jwtUtils.isRefreshToken(refreshToken)) { // Check "type" claim
                return null;
            }
            String userId = jwtUtils.getUserIdFromJwtToken(refreshToken); // Extract userId
            String newAccessToken = jwtUtils.generateTokenFromUserID(userId);
            String newRefreshToken= jwtUtils.generateRefreshToken(userId);
            return new JwtResponse(newAccessToken,newRefreshToken);
        } catch (Exception e) {
            return null;
        }
    }



    @Override
    public String getUsernameFromToken(String token) {
        return jwtUtils.getUserNameFromJwtToken(token);
    }

    @Override
    public boolean registerUser(RegisterRequest registerRequest) {
        Optional<Account> existingAccount = accountRepository.findByEmail(registerRequest.getEmail());
        if (existingAccount.isPresent()) {
            return false;
        }
        Account createdUser = new Account();
        createdUser.setEmail(registerRequest.getEmail());
        createdUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        createdUser.setPhone(registerRequest.getPhone());
        createdUser.setName(registerRequest.getName());
        createdUser.setRole(registerRequest.getRole());
        createdUser.setStatus(ActivationEnums.ACTIVE);
        accountRepository.save(createdUser);


        return true;
    }

    private boolean sendOtpEmail(String email, String name,String otp, String otptoken) {

        try {
            emailService.sendOtpEmail(email, name, otp, otptoken);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
    private String generateOTP() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        // Generate a 6-digit OTP
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10)); // Random digit between 0 and 9
        }

        return otp.toString();
    }
    @Override
    public String resendOtp(String email) {
        Optional<Account> accountOpt = accountRepository.findByEmail(email);
        if (accountOpt.isEmpty()) {
            return "Failed: Email not registered.";
        }
        Account account = accountOpt.get();
        String otp = generateOTP();
        String otpToken = jwtUtils.generateOtpToken(email, otp);
        boolean emailSent = sendOtpEmail(email, account.getName(), otp, otpToken);
        if (!emailSent) {
            return "Failed to resend OTP email.";
        }
        return otpToken;
    }
    @Override
    public JwtResponse handleGoogleLogin(String authCode) {
        String accessToken = googleService.exchangeCodeForAccessToken(authCode);
        Account user = processGoogleLogin(accessToken);

        return new JwtResponse(
                jwtUtils.generateTokenFromUserID(user.getId()),
                jwtUtils.generateRefreshToken(user.getId()),
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                user.getName(),
                user.getAddress(),
                user.getPhone(),
                user.getDateOfBirth(),
                user.getStatus()
        );
    }


    private Account processGoogleLogin(String accessToken) {
        Map<String, Object> userInfo = googleService.fetchGoogleUserInfo(accessToken);
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        return accountRepository.findByEmail(email).orElseGet(() -> {
            Account newUser = new Account();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setStatus(ActivationEnums.ACTIVE);
            newUser.setRole(RoleEnums.CUSTOMER);
            return accountRepository.save(newUser);
        });
    }

    @Override
    public boolean verifyOtp(String email, String otp, String token) {
        try {
            Account getacc=accountRepository.findByEmail(email).orElse(null);
            getacc.setStatus(ActivationEnums.ACTIVE);
            accountRepository.save(getacc);
            String extractedOtp = jwtUtils.getOtpFromToken(token);
            return extractedOtp.equals(otp);

        } catch (Exception e) {
            return false;
        }
    }

    private Account mapToEntity(AccountDTO dto) {
        Account account = new Account();
        account.setPassword(dto.getPassword());
        account.setEmail(dto.getEmail());
        account.setName(dto.getName());
        account.setAddress(dto.getAddress());
        account.setPhone(dto.getPhone());
        account.setRole(RoleEnums.ADMIN);
        account.setDateOfBirth(dto.getDateOfBirth());
        account.setStatus(ActivationEnums.ACTIVE);
        return account;
    }
    @Override
    public AccountDTO createUser(AccountDTO user) {
        Account newaccount= mapToEntity(user);
        accountRepository.save(newaccount);
        return user;
    }
    @Override
    public PaginatedResponseDTO<AccountDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Account> accounts = accountRepository.findAll(pageable);
        List<AccountDTO> accountDTOs = new ArrayList<>();
        for (Account account : accounts.getContent()) {
            accountDTOs.add(mapToDTO(account));
        }
        PaginatedResponseDTO<AccountDTO> response = new PaginatedResponseDTO<>();
        response.setContent(accountDTOs);
        response.setPageNumber(accounts.getNumber());
        response.setPageSize(accounts.getSize());
        response.setTotalElements(accounts.getTotalElements());
        response.setTotalPages(accounts.getTotalPages());

        return response;
    }


    @Override
    public AccountDTO getUserById(String id) {
        Account account= accountRepository.findById(id).orElse(null);
        return mapToDTO(account);
    }
    @Override
    public boolean updateUser(String id,AccountDTO user) {
        Account updatedAccount= accountRepository.findById(id).orElse(null);
        if(updatedAccount==null){
            return false;
        }
        updatedAccount=mapToEntity(user);
        accountRepository.save(updatedAccount);
        return true;
    }
    @Override
    public boolean deleteUser(String id) {
        Account existingAccount = accountRepository.getById(id);
        if (existingAccount != null) {
            accountRepository.delete(existingAccount);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public AccountCreationResponse createAccount(CreateAccountRequest request) {
        // Validate email uniqueness
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Generate password and account details
        String plainPassword = PasswordUtils.generateRandomPassword();
        String accountId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        // Create account
        Account account = createAccountEntity(request, accountId, plainPassword, now);
        Account savedAccount = accountRepository.save(account);

        // Send email
        sendWelcomeEmail(account.getEmail(), account.getName(), plainPassword);

        // Return response
        return mapToResponse(savedAccount);
    }

    private Account createAccountEntity(CreateAccountRequest request, String id, String plainPassword, LocalDateTime now) {
        Account account = new Account();
        account.setId(id);
        account.setEmail(request.getEmail());
        account.setName(request.getName());
        account.setPassword(passwordEncoder.encode(plainPassword));
        account.setPhone(request.getPhone());
        account.setAddress(request.getAddress());
        account.setRole(request.getRole());
        account.setDateOfBirth(request.getDateOfBirth());
        account.setStatus(ActivationEnums.INACTIVE);
        account.setCreatedAt(now);
        account.setUpdatedAt(now);
        return account;
    }

    private void sendWelcomeEmail(String email, String name, String password) {
        try {
            emailService.sendPasswordEmail(email, name, password);
        } catch (Exception e) {
            log.error("Failed to send welcome email to: {}", email, e);
            throw new RuntimeException("Failed to send welcome email");
        }
    }

    private AccountCreationResponse mapToResponse(Account account) {
        AccountCreationResponse response = new AccountCreationResponse();
        response.setId(UUID.fromString(account.getId()));
        response.setEmail(account.getEmail());
        response.setName(account.getName());
        response.setPhone(account.getPhone());
        response.setAddress(account.getAddress());
        response.setRole(account.getRole());
        response.setStatus(account.getStatus());
        response.setDateOfBirth(account.getDateOfBirth());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}
