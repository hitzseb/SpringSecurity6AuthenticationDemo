package com.hitzseb.jwtSpringSecurity6demo.controller;

import com.hitzseb.jwtSpringSecurity6demo.dto.CredentialsDto;
import com.hitzseb.jwtSpringSecurity6demo.responses.AuthenticationResponse;
import com.hitzseb.jwtSpringSecurity6demo.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody CredentialsDto credentialsDto
    ) {
        return ResponseEntity.ok(service.register(credentialsDto));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody CredentialsDto credentialsDto
    ) {
        return ResponseEntity.ok(service.authenticate(credentialsDto));
    }


}
