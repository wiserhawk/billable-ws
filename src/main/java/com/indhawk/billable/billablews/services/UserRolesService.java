package com.indhawk.billable.billablews.services;

import com.indhawk.billable.billablews.dao.UserRolesDao;
import com.indhawk.billable.models.UserRoleType;
import com.indhawk.billable.models.UserRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserRolesService {

    @Autowired
    private UserRolesDao userRolesDao;

    public boolean saveUserRoles(int userId, String roles) {
        return userRolesDao.saveUserRoles(userId, roles);
    }

    public List<String> getUserRolesByUserId(int userId) {
        UserRoles userRoles = userRolesDao.getUserRolesByUserId(userId);
        List<UserRoleType> roles = (userRoles != null) ? userRoles.getRoles() : new ArrayList<>();
        return roles.stream().map(roleType -> roleType.toString()).collect(Collectors.toList());
    }

}
