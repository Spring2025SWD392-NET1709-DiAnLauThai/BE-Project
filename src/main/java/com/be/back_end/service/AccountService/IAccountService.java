package com.be.back_end.service.AccountService;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.request.RegisterRequest;
import com.be.back_end.dto.response.JwtResponse;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.dto.response.TokenValidateDTO;
import com.be.back_end.model.Account;
import org.springframework.http.ResponseCookie;

import java.util.List;
import java.util.UUID;

public interface IAccountService {
    AccountDTO createUser(AccountDTO user);
    PaginatedResponseDTO<AccountDTO> getAllUsers(int page, int size);
    AccountDTO getUserById(String id);
    boolean updateUser(String id,AccountDTO user);
    boolean deleteUser(String id);
    boolean validateToken();
    String getUsernameFromToken(String token);
    boolean registerUser(RegisterRequest registerRequest);
    boolean verifyOtp(String email, String otp, String token);
    String resendOtp(String email);
    JwtResponse refreshAccessToken(String refreshToken);
}
