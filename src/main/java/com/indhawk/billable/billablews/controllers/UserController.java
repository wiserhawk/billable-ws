package com.indhawk.billable.billablews.controllers;

import com.indhawk.billable.billablews.auth.HasRoles;
import com.indhawk.billable.billablews.requests.UserConfigRequest;
import com.indhawk.billable.billablews.requests.UserRolesConfigRequest;
import com.indhawk.billable.billablews.responses.UserResponse;
import com.indhawk.billable.billablews.services.UserRolesService;
import com.indhawk.billable.billablews.services.UserService;
import com.indhawk.billable.billablews.utils.SecurityContextHelper;
import com.indhawk.billable.models.User;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@CrossOrigin("*")
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRolesService userRolesService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(produces = "application/json")
    @HasRoles(roles = {"SUPER_USER", "ADMIN_USER"})
    public List<UserResponse> getAllUsersOfOrg() {
        int orgId = SecurityContextHelper.getPrincipal().getOrgId();
        List<User> users = userService.getUsersByOrgId(orgId);
        return transformToUserResponse(users);
    }

    @PostMapping()
    public Boolean saveUser(@RequestBody UserConfigRequest userReq) {
        try {
            return userService.saveUser(userReq);
        } catch (Throwable th) {
            log.error("Failed to save user", th);
            return false;
        }
    }

    @PostMapping("/reset")
    @HasRoles(roles = {"SUPER_USER"})
    public Boolean resetUser(UserConfigRequest userReq) {
        try {
            return userService.saveUser(userReq);
        } catch (Throwable th) {
            log.error("Failed to save user", th);
            return false;
        }
    }

    @PostMapping("/{userId}/{orgId}}")
    @HasRoles(roles = {"SUPER_USER", "ADMIN_USER"})
    public Boolean linkUserToOrg(@PathVariable int userId, @PathVariable int orgId) {
        User principal = SecurityContextHelper.getPrincipal();
        if (principal == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        if (principal.getOrgId() != orgId) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }
        return userService.assignOrgToUser(userId, orgId);
    }

    @GetMapping(value = "/{userId}", produces = "application/json")
    @HasRoles(roles = {"SUPER_USER", "ADMIN_USER", "OPERATION_USER"})
    public UserResponse getUser(@PathVariable int userId) {
        User user = userService.getUser(userId);
        return transformToUserResponse(user);
    }

    @GetMapping(value = "/allUsersInSystem", produces = "application/json")
    @HasRoles(roles = {"SUPER_USER"})
    public List<UserResponse> getUsers() {
        List<User> users = userService.getAllUsers();
        return transformToUserResponse(users);
    }

    @PostMapping("/roles")
    @HasRoles(roles = {"SUPER_USER", "ADMIN_USER"})
    public Boolean saveUserRoles(@RequestBody UserRolesConfigRequest userRoles) {
        return userRolesService.saveUserRoles(
                userRoles.getUserId(),
                userRoles.getRoles().stream().collect(Collectors.joining(",")));
    }

    @GetMapping(value = "/roles/{userId}", produces = "application/json")
    @HasRoles(roles = {"SUPER_USER", "ADMIN_USER"})
    public List<String> getUserRolesByUserId(@PathVariable("userId") int userId) {
        return userRolesService.getUserRolesByUserId(userId);
    }

    private List<UserResponse> transformToUserResponse(List<User> users) {
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            UserResponse u = modelMapper.map(user, UserResponse.class);
            userResponses.add(u);
        }
        return userResponses;
    }

    private UserResponse transformToUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

}
