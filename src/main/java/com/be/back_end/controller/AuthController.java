package com.be.back_end.controller;

import com.be.back_end.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody SigninRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AccountDetailsImpl accountDetails = (AccountDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(accountDetails);

        ApiResponse apiResponse = new ApiResponse(200, new JwtResponse(jwtCookie.getValue()), "Login successful");

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