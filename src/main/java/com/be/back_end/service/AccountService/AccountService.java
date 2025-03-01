package com.be.back_end.service.AccountService;


import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.request.CreateAccountRequest;
import com.be.back_end.dto.request.RegisterRequest;
import com.be.back_end.dto.request.UpdateAccountRequest;
import com.be.back_end.dto.request.UpdateProfileRequest;
import com.be.back_end.dto.response.AccountCreationResponse;
import com.be.back_end.dto.response.JwtResponse;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.dto.response.TokenValidateDTO;

import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import com.be.back_end.model.Account;
import com.be.back_end.repository.AccountRepository;

import com.be.back_end.security.jwt.JwtUtils;
import com.be.back_end.service.CloudinaryService.ICloudinaryService;
import com.be.back_end.service.EmailService.IEmailService;
import com.be.back_end.service.GoogleService.IGoogleService;
import com.be.back_end.utils.AccountUtils;
import com.be.back_end.utils.PasswordUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@Slf4j
public class AccountService implements IAccountService{
    /*private final IGenericService<Account, UUID> genericService;*/
    private final AccountUtils accountUtils;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final JwtUtils jwtUtils;
    private final IEmailService emailService;
    private final IGoogleService googleService;
    private final ICloudinaryService cloudinaryService;
    public AccountService(AccountUtils accountUtils, PasswordEncoder passwordEncoder, AccountRepository accountRepository, JwtUtils jwtUtils, IEmailService emailService, IGoogleService googleService, ICloudinaryService cloudinaryService) {
        this.accountUtils = accountUtils;
        this.passwordEncoder = passwordEncoder;

        this.accountRepository = accountRepository;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
        this.googleService = googleService;
        this.cloudinaryService = cloudinaryService;
    }
    private AccountDTO mapToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setEmail(account.getEmail());
        dto.setName(account.getName());
        dto.setAddress(account.getAddress());
        dto.setPhone(account.getPhone());
        dto.setRole(account.getRole());
        dto.setStatus(account.getStatus());
        dto.setImage_url(account.getImage_url());
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

    private Account mapToEntity(AccountDTO dto) {
        Account account = new Account();
        account.setEmail(dto.getEmail());
        account.setName(dto.getName());
        account.setAddress(dto.getAddress());
        account.setPhone(dto.getPhone());
        account.setRole(RoleEnums.ADMIN);
        account.setDateOfBirth(dto.getDateOfBirth());
        account.setStatus(ActivationEnums.ACTIVE);
        account.setImage_url(dto.getImage_url());
        return account;
    }
    @Override
    public AccountDTO createUser(AccountDTO user) {
        Account newaccount= mapToEntity(user);
        accountRepository.save(newaccount);
        return user;
    }
    @Override
    public PaginatedResponseDTO<AccountDTO> getAllUsers(String keyword,
                                                        int page,
                                                        int size,
                                                        RoleEnums role,
                                                        ActivationEnums status,
                                                        LocalDateTime dateFrom,
                                                        LocalDateTime dateTo,
                                                        String sortDir,
                                                        String sortBy) {
        Sort.Direction sort;
        if(sortDir.equals("asc")) {
            sort =Sort.Direction.ASC;
        }else{
        sort =Sort.Direction.DESC;}
        Pageable pageable = PageRequest.of(page-1, size,sort,sortBy);
        Page<Account> accounts;
        if (role != null && status != null && dateFrom != null && dateTo != null) {
            accounts = accountRepository.findByRoleAndStatusAndCreatedAtBetween(role, status, dateFrom, dateTo, pageable);
        }
        else if (role != null && status != null) {
            accounts = accountRepository.findByRoleAndStatus(role, status, pageable);
        }
        else if (role != null && dateFrom != null && dateTo != null) {
            accounts = accountRepository.findByRoleAndCreatedAtBetween(role, dateFrom, dateTo, pageable);
        }
        else if (status != null && dateFrom != null && dateTo != null) {
            accounts = accountRepository.findByStatusAndCreatedAtBetween(status, dateFrom, dateTo, pageable);
        }
        else if (role != null) {
            accounts = accountRepository.findByRole(role, pageable);
        }
        else if (status != null) {
            accounts = accountRepository.findByStatus(status, pageable);
        }
        else if (dateFrom != null && dateTo != null) {
            accounts = accountRepository.findByCreatedAtBetween(dateFrom, dateTo, pageable);
        }
        else if (keyword != null && !keyword.trim().isEmpty()) {
            accounts = accountRepository.findByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(keyword, keyword, pageable);
        }
        else {
            accounts = accountRepository.findAll(pageable);
        }
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
    public boolean updateUser(UpdateAccountRequest user) {
        Account updatedAccount= accountRepository.findById(user.getId().toString()).orElse(null);
        if(updatedAccount==null){
            return false;
        }
        updatedAccount.setUpdatedAt(LocalDateTime.now());
        updatedAccount.setRole(user.getRole());
        updatedAccount.setStatus(user.getStatus());
        accountRepository.save(updatedAccount);
        return true;
    }
    @Override
    public boolean updateProfile(UpdateProfileRequest user, MultipartFile image) {
        if (user.getId() == null || user.getId().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        Account existingAccount = accountRepository.findById(user.getId()).orElse(null);
        if (existingAccount == null) {
            return false; // Account not found
        }

        if (image != null && !image.isEmpty()) {
            String imageurl = cloudinaryService.uploadFile(image);
            if (imageurl != null) {
                existingAccount.setImage_url(imageurl);
            }
        }

        existingAccount.setUpdatedAt(LocalDateTime.now());
        existingAccount.setPhone(user.getPhone());
        existingAccount.setDateOfBirth(user.getDateOfBirth());
        existingAccount.setEmail(user.getEmail());
        existingAccount.setName(user.getName());
        existingAccount.setAddress(user.getAddress());

        accountRepository.save(existingAccount);
        return true;
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
