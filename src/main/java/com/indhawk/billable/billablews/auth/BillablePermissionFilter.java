package com.indhawk.billable.billablews.auth;

import com.indhawk.billable.billablews.utils.SecurityContextHelper;
import com.indhawk.billable.models.UserRoleType;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;

// uncomment bellow annotation to enable this filter
//@Component
public class BillablePermissionFilter implements Filter {

    // INACTIVE FOR NOW
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        /*List<GrantedAuthority> userRoles = SecurityContextHelper.getGrantedAuthority();
        boolean authorized = false;
        for (UserRoleType role : UserRoleType.values()) {
            authorized = userRoles.contains(new SimpleGrantedAuthority(role.name()));
            if (authorized) break;
        }
        if (!authorized) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }

        chain.doFilter(request, response);*/
    }
}
