package com.be.back_end.controller;

import com.be.back_end.dto.request.RefreshTokenRequest;
import com.be.back_end.dto.request.RegisterRequest;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.ErrorResponse;
import com.be.back_end.dto.response.TokenValidateDTO;
import com.be.back_end.service.AccountService.IAccountService;
import com.be.back_end.service.GoogleService.IGoogleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.be.back_end.dto.request.SigninRequest;
import com.be.back_end.dto.response.JwtResponse;
import com.be.back_end.config.jwt.JwtUtils;
import com.be.back_end.service.AccountService.AccountDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.util.List;

import static org.springframework.web.servlet.function.ServerResponse.status;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@SecurityRequirement(name = "Bearer Authentication")
public class AuthController {
    @Autowired
    private IGoogleService googleService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private IAccountService accountService;
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<TokenValidateDTO>> validateToken() {
        boolean response = accountService.validateToken();
        return response
                ? ResponseEntity.ok(new ApiResponse<>(200, null, "Token is valid"))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(401, null, "Token is invalid or expired"));
    }
    @GetMapping("/google/login")
    public ResponseEntity<?> getGoogleAuthUrl() {
        try {
            String authUrl = googleService.generateGoogleAuthUrl();
            return ResponseEntity.ok(new ApiResponse<>(200, authUrl, "Google Auth URL generated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Failed to generate Google Auth URL", List.of(e.getMessage())));
        }
    }

    @GetMapping("/google/callback")
    public ResponseEntity<?> googleCallback(@RequestParam("code") String code) {
        try {
            JwtResponse jwtResponse = accountService.handleGoogleLogin(code);
            return ResponseEntity.ok(new ApiResponse<>(200, jwtResponse, "Login successful"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "An error occurred during login", List.of(e.getMessage())));
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        boolean registeringUser = accountService.registerUser(registerRequest);
        if (!registeringUser) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, null, List.of("Account failed to be created")));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Account created"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
        JwtResponse jwtResponse = accountService.refreshAccessToken(refreshToken);

        if (jwtResponse == null || jwtResponse.getAccessToken() == null) {
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
        
        ApiResponse apiResponse = new ApiResponse(200, new JwtResponse(jwtCookie.getValue(), refreshToken),
                "Login successful");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(apiResponse);
    }
    
    @PostMapping("/signout")
    public ResponseEntity<?> signOut() {
        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ApiResponse apiResponse = new ApiResponse(200, "", "Logout successful");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(apiResponse);
    }
}