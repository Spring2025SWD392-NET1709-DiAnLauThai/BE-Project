package com.be.back_end.controller;

import com.be.back_end.dto.request.RegisterRequest;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.ErrorResponse;
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
import com.be.back_end.model.Account;
import com.be.back_end.security.jwt.JwtUtils;
import com.be.back_end.service.AccountService.AccountDetailsImpl;
import com.be.back_end.service.AccountService.AccountDetailsServiceImpl;
import com.be.back_end.service.AccountService.AccountService;
import org.springframework.security.core.Authentication;

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
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        return accountService.validateToken(token)
                ? ResponseEntity.ok(new ApiResponse(200, accountService.getUsernameFromToken(token), "Token is valid"))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(401, null, "Invalid Token"));
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

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody SigninRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtCookie((AccountDetailsImpl) authentication.getPrincipal()).toString();
        ApiResponse apiResponse = new ApiResponse(200, new JwtResponse(jwt), "Login successful");
        return ResponseEntity.ok(apiResponse);
    }
}