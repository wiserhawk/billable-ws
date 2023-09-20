package com.indhawk.billable.billablews.requests;


import com.indhawk.billable.billablews.auth.HasRoles;
import com.indhawk.billable.models.UserRoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/roles")
public class RoleController {

    @GetMapping(produces = "application/json")
    @HasRoles(roles = {"SUPER_USER", "ADMIN_USER"})
    public List<String> getRoles() {
        List<String> roles = new ArrayList<>();
        for (UserRoleType role : UserRoleType.values()) {
            if (role.name() != "SUPER_USER") {
                roles.add(role.name());
            }
        }
        return roles;
    }
}
