package com.indhawk.billable.billablews.configs;

import com.indhawk.billable.billablews.services.UserRolesService;
import com.indhawk.billable.billablews.services.UserService;
import com.indhawk.billable.billablews.utils.JwtTokenUtils;
import com.indhawk.billable.billablews.utils.RequestPermissionHelper;
import com.indhawk.billable.models.User;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRolesService userRolesService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
//        if (RequestPermissionHelper.isRequestPermitted(request)) {
//            log.info("Request = {} is permitted without authorization", request.getRequestURI());
//            filterChain.doFilter(request, response);
//            return;
//        }
        log.info("JwtRequestFilter in action");
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtils.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.error("JWT Token has expired");
            }
        } else {
            log.warn("JWT Token does not begin with Bearer String");
        }

        //Once we get the token validate it.
        if (username != null) {
            User user = userService.getUserByName(username);

            // if token is valid configure Spring Security to manually set authentication
            if (jwtTokenUtils.validateToken(jwtToken, user)) {

                Authentication auth = createAuthentication(user, request);
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }

    private Authentication createAuthentication(User user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user,
                null,
                getGrantedAuthority(Integer.parseInt(user.getUserId())));
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return auth;
    }

    private List<GrantedAuthority> getGrantedAuthority(int userId) {
        return userRolesService.getUserRolesByUserId(userId)
                .stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }
}
