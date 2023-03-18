package com.hitzseb.jwtSpringSecurity6demo.service;

import com.hitzseb.jwtSpringSecurity6demo.Exceptions.UserAlreadyExistsException;
import com.hitzseb.jwtSpringSecurity6demo.enums.TokenType;
import com.hitzseb.jwtSpringSecurity6demo.responses.AuthenticationResponse;
import com.hitzseb.jwtSpringSecurity6demo.dto.CredentialsDto;
import com.hitzseb.jwtSpringSecurity6demo.model.Token;
import com.hitzseb.jwtSpringSecurity6demo.repository.TokenRepository;
import com.hitzseb.jwtSpringSecurity6demo.enums.UserRole;
import com.hitzseb.jwtSpringSecurity6demo.model.User;
import com.hitzseb.jwtSpringSecurity6demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(CredentialsDto credentialsDto) {
        User existingUser = userRepository.findByEmail(credentialsDto.email()).orElse(null);
        if (existingUser != null) {
            throw new UserAlreadyExistsException("El usuario ya está registrado");
        }
        User user = new User();
        user.setEmail(credentialsDto.email());
        user.setPassword(passwordEncoder.encode(credentialsDto.password()));
        user.setUserRole(UserRole.USER);
        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        AuthenticationResponse response = new AuthenticationResponse(jwtToken);
        return response;
    }

    public AuthenticationResponse authenticate(CredentialsDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User user = userRepository.findByEmail(request.email()).orElse(null);
        String jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        AuthenticationResponse response = new AuthenticationResponse(jwtToken);
        return response;
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = new Token();
        token.setUser(user);
        token.setToken(jwtToken);
        token.setTokenType(TokenType.BEARER);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
