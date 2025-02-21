package com.be.back_end.service.AccountService;


import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.request.RegisterRequest;
import com.be.back_end.dto.response.JwtResponse;
import com.be.back_end.dto.response.TokenValidateDTO;
import com.be.back_end.enums.AccountEnums;
import com.be.back_end.enums.RoleEnums;
import com.be.back_end.model.Account;
import com.be.back_end.repository.AccountRepository;

import com.be.back_end.security.jwt.JwtUtils;
import com.be.back_end.service.EmailService.IEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService implements IAccountService{
    /*private final IGenericService<Account, UUID> genericService;*/
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final JwtUtils jwtUtils;
    private final IEmailService emailService;
    public AccountService(PasswordEncoder passwordEncoder, AccountRepository accountRepository, JwtUtils jwtUtils, IEmailService emailService) {
        this.passwordEncoder = passwordEncoder;

        this.accountRepository = accountRepository;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
    }

    private AccountDTO mapToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(UUID.fromString(account.getId().toString()));
        dto.setEmail(account.getEmail());
        dto.setName(account.getName());
        dto.setAddress(account.getAddress());
        dto.setPhone(account.getPhone());
        // dto.setPassword(account.getPassword());
        dto.setRole(account.getRole().toString());
        dto.setDateOfBirth(account.getDateOfBirth());
        return dto;
    }


    @Override
    public TokenValidateDTO validateToken(String token) {
        if (jwtUtils.validateJwtToken(token)) {
            return new TokenValidateDTO(true);
        }
        return new TokenValidateDTO(false);
    }

    @Override
    public JwtResponse refreshAccessToken(String refreshToken) {
        try {
            String subject = jwtUtils.getUserIdFromJwtToken(refreshToken);
            if (!subject.startsWith("REFRESH-")) {
                return null;
            }
            String userId = subject.replace("REFRESH-", "");
            String newAccessToken = jwtUtils.generateTokenFromUserID(userId);
            String newRefreshToken = jwtUtils.generateRefreshToken(userId);
            return new JwtResponse(newAccessToken, newRefreshToken);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public String getUsernameFromToken(String token) {
        return jwtUtils.getUserNameFromJwtToken(token);
    }

    @Override
    public String registerUser(RegisterRequest registerRequest) {
        Optional<Account> existingAccount = accountRepository.findByEmail(registerRequest.getEmail());
        if (existingAccount.isPresent()) {
            return null;
        }
        Account createdUser = new Account();
        createdUser.setEmail(registerRequest.getEmail());
        createdUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        createdUser.setAddress(registerRequest.getAddress());
        createdUser.setPhone(registerRequest.getPhone());
        createdUser.setDateOfBirth(registerRequest.getDateOfBirth());
        createdUser.setName(registerRequest.getName());
        createdUser.setRole(RoleEnums.CUSTOMER);
        createdUser.setStatus(AccountEnums.INACTIVE);
        accountRepository.save(createdUser);
        String otp = generateOTP();
        String otpToken = jwtUtils.generateOtpToken(registerRequest.getEmail(), otp);
        boolean emailSent = sendOtpEmail(createdUser.getEmail(), createdUser.getName(), otp, otpToken);
        return emailSent ? otpToken : null;
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
    public boolean verifyOtp(String email, String otp, String token) {
        try {
            Account getacc=accountRepository.findByEmail(email).orElse(null);
            getacc.setStatus(AccountEnums.ACTIVE);
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
        account.setStatus(AccountEnums.ACTIVE);
        return account;
    }
    @Override
    public AccountDTO createUser(AccountDTO user) {
        Account newaccount= mapToEntity(user);
        accountRepository.save(newaccount);
        return user;
    }
    @Override
    public List<AccountDTO> getAllUsers() {
        List<Account> accounts= accountRepository.findAll();
        List<AccountDTO> list= new ArrayList<>();
        for(Account acc:accounts)
        {
            list.add(mapToDTO(acc));
            System.out.println(acc.getId());
        }
        return list;
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
}
