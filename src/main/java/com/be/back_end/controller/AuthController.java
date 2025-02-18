package com.be.back_end.controller;

import com.be.back_end.dto.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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