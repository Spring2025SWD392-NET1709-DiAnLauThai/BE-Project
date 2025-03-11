package com.be.back_end.service.AccountService;

import com.be.back_end.dto.request.AccountDTO;
import com.be.back_end.dto.request.CreateAccountRequest;
import com.be.back_end.dto.request.RegisterRequest;
import com.be.back_end.dto.request.UpdateAccountRequest;
import com.be.back_end.dto.request.UpdateProfileRequest;
import com.be.back_end.dto.response.AccountCreationResponse;
import com.be.back_end.dto.response.JwtResponse;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public interface IAccountService {
    AccountDTO createUser(AccountDTO user);
    PaginatedResponseDTO<AccountDTO> getAllUsers(String keyword,
                                                 int page,
                                                 int size,
                                                 RoleEnums role,
                                                 LocalDateTime dateFrom,
                                                 LocalDateTime dateTo,
                                                 ActivationEnums activationEnums,
                                                 String sortDir,
                                                 String sortBy);
    AccountDTO getUserById(String id);
    boolean updateUser(UpdateAccountRequest user);
    boolean updateProfile(UpdateProfileRequest user, MultipartFile image);
    boolean validateToken();
    String getUsernameFromToken(String token);
    boolean registerUser(RegisterRequest registerRequest);
    JwtResponse refreshAccessToken(String refreshToken);
    AccountCreationResponse createAccount(CreateAccountRequest request);
    JwtResponse handleGoogleLogin(String authCode);
}
