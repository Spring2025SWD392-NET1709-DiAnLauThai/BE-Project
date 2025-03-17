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
    public ResponseEntity<ApiResponse<String>> getGoogleAuthUrl() {
        return ResponseEntity.ok(new ApiResponse<>(200, googleService.generateGoogleAuthUrl(), "Google Auth URL generated successfully"));
    }

    @GetMapping("/google/callback")
    public ResponseEntity<ApiResponse<JwtResponse>> googleCallback(@RequestParam("code") String code) {
        JwtResponse jwtResponse = accountService.handleGoogleLogin(code);
        return ResponseEntity.ok(new ApiResponse<>(200, jwtResponse, "Login successful"));
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