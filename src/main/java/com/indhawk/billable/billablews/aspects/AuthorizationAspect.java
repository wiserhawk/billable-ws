package com.indhawk.billable.billablews.aspects;

import com.indhawk.billable.billablews.auth.HasRoles;
import com.indhawk.billable.billablews.utils.SecurityContextHelper;
import com.indhawk.billable.models.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import java.util.List;

@Aspect
@Component
public class AuthorizationAspect {

    @Around("@annotation(hasRoles)")
    public Object authorize(ProceedingJoinPoint joinPoint, HasRoles hasRoles) throws Throwable {
        if (hasRoles.roles().length > 0) {
            User user = SecurityContextHelper.getPrincipal();
            if (user == null) {
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
            }
            List<GrantedAuthority> userRoles = SecurityContextHelper.getGrantedAuthority();
            boolean authorized = false;
            for (String role : hasRoles.roles()) {
                authorized = userRoles.contains(new SimpleGrantedAuthority(role));
                if (authorized) break;
            }
            if (!authorized) {
                throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
            }
        }
        return joinPoint.proceed();
    }

}
