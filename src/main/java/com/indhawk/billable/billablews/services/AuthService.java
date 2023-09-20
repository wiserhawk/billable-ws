package com.indhawk.billable.billablews.services;

import com.indhawk.billable.billablews.dao.UserDao;
import com.indhawk.billable.billablews.utils.CommonUtils;
import com.indhawk.billable.billablews.utils.JwtTokenUtils;
import com.indhawk.billable.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserDao userDao;

    public User getUserForAuthenticate(String userName) {
        User user = null;
        if (CommonUtils.isEmptyString(userName)) {
            log.warn("Invalid username={}", userName);
            throw new RuntimeException("Invalid username or password");
        }
        else if (CommonUtils.patternMatches(userName, CommonUtils.EMAIL_REGEX)) {
            user = userDao.getAuthenticate(null, userName);
        } else {
            user = userDao.getAuthenticate(userName, null);
        }
        return user;
    }
}
