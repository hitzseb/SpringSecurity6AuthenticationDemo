package com.hitzseb.authentication.controller;

import com.hitzseb.authentication.service.AuthenticationResponse;
import com.hitzseb.authentication.service.AuthenticationService;
import com.hitzseb.authentication.service.Credentials;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody Credentials credentials) {
        return ResponseEntity.ok(authenticationService.register(credentials));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody Credentials credentials) {
        return ResponseEntity.ok(authenticationService.authenticate(credentials));
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        return authenticationService.confirmToken(token);
    }

}
