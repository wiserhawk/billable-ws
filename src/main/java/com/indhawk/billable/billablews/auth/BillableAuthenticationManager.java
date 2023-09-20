package com.indhawk.billable.billablews.auth;

import com.indhawk.billable.billablews.services.AuthService;
import com.indhawk.billable.billablews.utils.HashingUtils;
import com.indhawk.billable.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

@Component
@Slf4j
public class BillableAuthenticationManager implements AuthenticationManager {

    @Autowired
    private AuthService authService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        User user = authService.getUserForAuthenticate(username);
        if (user != null) {
            if (!user.isActive()) {
                throw new DisabledException("User is inactive.");
            }
            String hash = null;
            try {
                byte[] hashBytes = HashingUtils.getSHA(password);
                hash = HashingUtils.bytesToHex(hashBytes);
            } catch (NoSuchAlgorithmException e) {
                log.error("Failed to convert password to hash", e);
                throw new RuntimeException("Failed to authenticate", e);
            }
            if (user.getUserName().equals(username) && user.getPassword().equals(hash)) {
                return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
            }
        }
        throw new BadCredentialsException("User credentials are not valid");

    }
}
