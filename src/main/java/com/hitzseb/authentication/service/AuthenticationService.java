package com.hitzseb.authentication.service;

import com.hitzseb.authentication.model.Token;
import com.hitzseb.authentication.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailSender;
    private static final String serverUrl = "http://localhost:8080/";

    public AuthenticationResponse register(Credentials credentials) {
        String email = credentials.email();
        String password = credentials.password();
        if (userService.findUserByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(String.format("Email address %s is already registered", email));
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userService.saveUser(user);
        String jwtToken = jwtService.generateToken(user);
        tokenService.saveUserToken(user, jwtToken);
        AuthenticationResponse response = new AuthenticationResponse(jwtToken);
        String link = serverUrl + "api/v1/confirm?token=" + jwtToken;
        emailSender.send(email, buildEmail(email, link));
        return response;
    }

    public AuthenticationResponse authenticate(Credentials credentials) {
        String email = credentials.email();
        String password = credentials.password();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        User user = userService.findUserByEmail(email).orElse(null);
        String jwtToken = jwtService.generateToken(user);
        assert user != null;
        tokenService.revokeAllUserTokens(user);
        tokenService.saveUserToken(user, jwtToken);
        return new AuthenticationResponse(jwtToken);
    }

    public String confirmToken(String authToken) {
        try {
            Token token = tokenService.findByToken(authToken);
            if (tokenService.isValid(token)) {
                userService.enableUser(token.getUser().getEmail());
                return "Confirmed";
            } else {
                return "Invalid token";
            }
        } catch (EntityNotFoundException e) {
            return "Token not found";
        }
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n"
                + "\n"
                + "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n"
                + "\n"
                + "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
                + "    <tbody><tr>\n"
                + "      <td width=\"100%\" height=\"53\" bgcolor=\"#198754\">\n"
                + "        \n"
                + "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n"
                + "          <tbody><tr>\n"
                + "            <td width=\"70\" bgcolor=\"#198754\" valign=\"middle\">\n"
                + "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
                + "                  <tbody><tr>\n"
                + "                    <td style=\"padding-left:10px\">\n"
                + "                  \n"
                + "                    </td>\n"
                + "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n"
                + "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirma tu email</span>\n"
                + "                    </td>\n"
                + "                  </tr>\n"
                + "                </tbody></table>\n"
                + "              </a>\n"
                + "            </td>\n"
                + "          </tr>\n"
                + "        </tbody></table>\n"
                + "        \n"
                + "      </td>\n"
                + "    </tr>\n"
                + "  </tbody></table>\n"
                + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
                + "    <tbody><tr>\n"
                + "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n"
                + "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n"
                + "    </tr>\n"
                + "  </tbody></table>\n"
                + "\n"
                + "\n"
                + "\n"
                + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
                + "    <tbody><tr>\n"
                + "      <td height=\"30\"><br></td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td width=\"10\" valign=\"middle\"><br></td>\n"
                + "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n"
                + "        \n"
                + "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hola " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Gracias por registrarte. Por favor, haz click para activar tu cuenta: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activar</a> </p></blockquote>\n <p>Hasta pronto</p>"
                + "        \n"
                + "      </td>\n"
                + "      <td width=\"10\" valign=\"middle\"><br></td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td height=\"30\"><br></td>\n"
                + "    </tr>\n"
                + "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n"
                + "\n"
                + "</div></div>";
    }

}
