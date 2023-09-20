package com.indhawk.billable.billablews.controllers;

import com.indhawk.billable.billablews.services.UserService;
import com.indhawk.billable.billablews.utils.JwtTokenUtils;
import com.indhawk.billable.models.User;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    // https://www.techgeeknext.com/spring/spring-boot-security-token-authentication-jwt

    @PostMapping(value = "/authenticate", produces = "application/json")
    public ResponseEntity<Pair> authenticate(@RequestBody Credential cred) throws Exception {
        authenticateUser(cred.getUsername(), cred.getPassword());
        User user = userService.getUserByName(cred.getUsername());
        final String jwt = jwtTokenUtils.generateToken(user);
        if (jwt != null)
            return ResponseEntity.ok(new Pair("token", jwt));
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private void authenticateUser(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @Data
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private static class Credential {
        private String username;
        private String password;
    }

    @Data
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private static class Pair {
        private String key;
        private String value;
    }

}
