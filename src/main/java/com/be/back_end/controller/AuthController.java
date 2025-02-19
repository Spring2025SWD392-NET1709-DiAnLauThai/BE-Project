package com.be.back_end.controller;

import com.be.back_end.dto.request.RegisterRequest;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.ErrorResponse;
import com.be.back_end.dto.response.TokenValidateDTO;
import com.be.back_end.service.AccountService.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.be.back_end.dto.request.SigninRequest;
import com.be.back_end.dto.response.JwtResponse;
import com.be.back_end.security.jwt.JwtUtils;
import com.be.back_end.service.AccountService.AccountDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private IAccountService accountService;
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<TokenValidateDTO>> validateToken(@RequestParam String token) {
        TokenValidateDTO response = accountService.validateToken(token);
        return response.isValid()
                ? ResponseEntity.ok(new ApiResponse<>(200, response, "Token is valid"))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(401, response, "Token is invalid or expired"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp, @RequestParam String token) {
        boolean verified = accountService.verifyOtp(email, otp, token);

        if (verified)
        {
            return ResponseEntity.ok(new ApiResponse<>(200,null,"Verified"));
        }
        return ResponseEntity.status(400).body(new ApiResponse<>(400,null,"otp is expired. Please check or resend otp"));
    }
    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<String>> resendOtp(@RequestParam String email) {
        String otpToken = accountService.resendOtp(email);
        if (otpToken.startsWith("Failed")) {
            return ResponseEntity.status(400).body(new ApiResponse<>(400, null, otpToken));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, otpToken, "OTP resent successfully."));
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody RegisterRequest registerRequest) {
        String otpToken = accountService.registerUser(registerRequest);
        if (otpToken == null) {
            return ResponseEntity.status(400).body(new ApiResponse<>(400, null, "Account created, but failed to send OTP email."));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, otpToken, "Account created! OTP Token generated successfully."));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        JwtResponse jwtResponse = accountService.refreshAccessToken(refreshToken);

        if (jwtResponse == null || jwtResponse.getToken() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(401, null, "Invalid or expired refresh token"));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, jwtResponse, "Token refreshed successfully"));
    }



    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody SigninRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AccountDetailsImpl accountDetails = (AccountDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(accountDetails);
        String refreshToken = jwtUtils.generateRefreshToken(accountDetails.getId());
        ApiResponse apiResponse = new ApiResponse(200, new JwtResponse(jwtCookie.getValue(),refreshToken), "Login successful");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(apiResponse);
    }

    // @GetMapping("/me") //Get user personal details
    // public ResponseEntity<?> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String authHeader) {

    //         if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    //             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token provided");
    //         }

    //         String token = authHeader.substring(7);

    //         if (!jwtUtils.validateJwtToken(token)) {
    //             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
    //         }

    //         String userId = jwtUtils.getUserIdFromJwtToken(token);

    //         Optional<Account> account = accountRepository.findById(userId);
    //         if (!account.isPresent()) {
    //             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    //         }

    //         Account user = account.get();
    //         ApiResponse apiResponse = new ApiResponse(200, new JwtResponse(
    //             user.getId(),
    //             user.getEmail(),
    //             user.getRole().name(),
    //             user.getName(),
    //             user.getAddress(),
    //             user.getPhone(),
    //             user.getDateOfBirth(),
    //             user.getStatus()
    //         ), "User details retrieved successfully");
    //         return ResponseEntity.ok(apiResponse);

    // }
}