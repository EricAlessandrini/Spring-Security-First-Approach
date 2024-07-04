package com.security.practice.controller;

import com.security.practice.domain.dto.AuthenticationLoginRequest;
import com.security.practice.domain.dto.AuthenticationResponse;
import com.security.practice.domain.dto.AuthenticationSignUpRequest;
import com.security.practice.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailsServiceImpl userDetails;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody @Valid AuthenticationLoginRequest loginRequest) {
        return new ResponseEntity<>(userDetails.loginUser(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signUpUser(@RequestBody @Valid AuthenticationSignUpRequest signUpRequest) {
        return new ResponseEntity<>(userDetails.signUpUser(signUpRequest), HttpStatus.CREATED);
    }

}
