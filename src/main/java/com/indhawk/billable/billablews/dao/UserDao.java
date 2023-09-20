package com.indhawk.billable.billablews.dao;

import com.indhawk.billable.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean saveUser(User user) {
        SqlParameterSource paramSrc = new MapSqlParameterSource()
                .addValue("user_name", user.getUserName())
                .addValue("name", user.getName())
                .addValue("surname", user.getSurname())
                .addValue("pwd", user.getPassword())
                .addValue("emailId", user.getEmail())
                .addValue("isActive", user.isActive())
                .addValue("singleSignOn", user.isSso())
                .addValue("org_id", user.getOrgId());

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withProcedureName("saveUser");
        Map<String, Object> out = call.execute(paramSrc);
        return true;
    }

    public User getUser(int userId) {
        String function = "SELECT * FROM getUser(?)";
        try {
            return jdbcTemplate.queryForObject(
                    function,
                    new UserRowMapper(),
                    new Object[]{userId});
        } catch (EmptyResultDataAccessException ex) {
            log.error("No user found for userId={}", userId, ex);
            return null;
        } catch (Exception ex) {
            log.error("Error while getting User for userId={}", userId, ex);
            return null;
        }
    }

    public User getUserByName(String userName) {
        String function = "SELECT * FROM getUserByName(?)";
        try {
            return jdbcTemplate.queryForObject(
                    function,
                    new UserRowMapper(),
                    new Object[]{userName});
        } catch (EmptyResultDataAccessException ex) {
            log.error("No user found for userName={}", userName);
            return null;
        } catch (Exception ex) {
            log.error("Error while getting User for userName={}", userName, ex);
            return null;
        }

    }

    public User getAuthenticate(String userName, String email) {
        String function = "SELECT * FROM getAuthenticate(?, ?)";
        try {
            return jdbcTemplate.queryForObject(
                    function,
                    new UserRowMapper(),
                    new Object[]{userName, email});
        } catch (EmptyResultDataAccessException ex) {
            log.error("Authentication failed for userName={}", userName);
            return null;
        } catch (Exception ex) {
            log.error("Error while authenticating username={} or email={}", userName, email, ex);
            return null;
        }
    }

    public List<User> getAllUsers() {
        String function = "SELECT * FROM getUser(?)";
       try {
           return jdbcTemplate.query(
                   function,
                   new UserRowMapper(),
                   new Object[]{null});
       } catch (EmptyResultDataAccessException ex) {
           log.error("No user found in database");
           return new ArrayList<>();
       } catch (Exception ex) {
           log.error("Error while getting all users", ex);
           return new ArrayList<>();
       }
    }

    public List<User> getUsersByOrgId(int orgId) {
        String function = "SELECT * FROM GetUsersByOrg(?)";
        try {
            return jdbcTemplate.query(
                    function,
                    new UserRowMapper(),
                    new Object[]{orgId});
        } catch (EmptyResultDataAccessException ex) {
            log.error("No user found for orgId={}", orgId);
            return new ArrayList<>();
        } catch (Exception ex) {
            log.error("Error while getting users for orgId={}", orgId, ex);
            return new ArrayList<>();
        }
    }

    private class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User u = User.builder()
                    .userId(rs.getString("userid"))
                    .name(rs.getString("name"))
                    .surname(rs.getString("surname"))
                    .userName(rs.getString("username"))
                    .password(rs.getString("password"))
                    .email(rs.getString("email"))
                    .createdOn(rs.getTimestamp("createdon").toLocalDateTime())
                    .lastLoginOn(rs.getTimestamp("lastlogin").toLocalDateTime())
                    // There is a bug in pgResultSet which retuns opposite boolean values.
                    .sso(!rs.getBoolean("sso"))
                    // There is a bug in pgResultSet which retuns opposite boolean values.
                    .active(!rs.getBoolean("active"))
                    .orgId(rs.getInt("orgid"))
                    .build();
            return u;
        }
    }

}
