package com.indhawk.billable.billablews.utils;

import com.indhawk.billable.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class SecurityContextHelper {

    public static User getPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  (principal instanceof User) ? (User) principal : null;
    }

    public static List<GrantedAuthority> getGrantedAuthority() {
        return (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    public static int getCurrentUserOrgId() {
        return getPrincipal().getOrgId();
    }

}
