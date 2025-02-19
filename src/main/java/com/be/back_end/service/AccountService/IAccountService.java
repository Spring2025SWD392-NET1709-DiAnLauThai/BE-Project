package com.be.back_end.service.AccountService;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.request.RegisterRequest;
import com.be.back_end.model.Account;

import java.util.List;
import java.util.UUID;

public interface IAccountService {
    AccountDTO createUser(AccountDTO user);
    List<AccountDTO> getAllUsers();
    AccountDTO getUserById(String id);
    boolean updateUser(String id,AccountDTO user);
    boolean deleteUser(String id);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    String registerUser(RegisterRequest registerRequest);
    boolean verifyOtp(String email, String otp, String token);
    String resendOtp(String email);
}
