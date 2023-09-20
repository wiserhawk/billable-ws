package com.indhawk.billable.billablews.services;

import com.indhawk.billable.billablews.dao.UserDao;
import com.indhawk.billable.billablews.requests.UserConfigRequest;
import com.indhawk.billable.billablews.utils.CommonUtils;
import com.indhawk.billable.billablews.utils.HashingUtils;
import com.indhawk.billable.billablews.utils.SecurityContextHelper;
import com.indhawk.billable.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserDao userDao;

    // External save user request (From UI)
    public boolean saveUser(UserConfigRequest userReq) throws NoSuchAlgorithmException {
        if (CommonUtils.isEmptyString(userReq.getUsername()) ||
                CommonUtils.isEmptyString(userReq.getEmail()) ||
                CommonUtils.isEmptyString(userReq.getPassword())
        ) {
            throw new RuntimeException("User details not found");
        }

        User principal = SecurityContextHelper.getPrincipal();
        String password = HashingUtils.bytesToHex(HashingUtils.getSHA(userReq.getPassword()));

        User user = User.builder().userName(userReq.getUsername())
                .name(userReq.getName())
                .surname(userReq.getSurname())
                .email(userReq.getEmail())
                .password(password)
                .sso(false)
                .active(true)
                .orgId((principal != null) ? principal.getOrgId() : -1)
                .build();
        return userDao.saveUser(user);
    }

    // Internal save user request.
    public boolean saveUser(User user) {
        return userDao.saveUser(user);
    }


    public User getUser(int userId) {
        return userDao.getUser(userId);
    }

    public User getUserByName(String userName) {
        return userDao.getUserByName(userName);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public boolean assignOrgToUser(int userId, int orgId) {
        User user = userDao.getUser(userId)
                .toBuilder().orgId(orgId)
                .build();
        return userDao.saveUser(user);
    }

    public List<User> getUsersByOrgId(int orgId) {
        return userDao.getUsersByOrgId(orgId);
    }

}
