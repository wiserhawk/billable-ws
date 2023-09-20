package com.indhawk.billable.billablews.dao;

import com.indhawk.billable.billablews.utils.CommonUtils;
import com.indhawk.billable.models.UserRoleType;
import com.indhawk.billable.models.UserRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class UserRolesDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean saveUserRoles(int userId, String roles) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("role_names", roles);
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SaveUserRoles");
        call.execute(params);
        return true;
    }

    public UserRoles getUserRolesByUserId(int userId) {
        try {
            String function = "SELECT * FROM GetUserRoles(?)";
            return jdbcTemplate.queryForObject(
                    function,
                    new UserRolesMapper(),
                    new Object[]{userId});
        } catch (Exception ex) {
            log.error("Error while getting roles for userId={}", userId);
            return null;
        }
    }

    private class UserRolesMapper implements RowMapper<UserRoles> {

        @Override
        public UserRoles mapRow(ResultSet rs, int rowNum) throws SQLException {
            int userId = rs.getInt("userid");
            String[] roles =  CommonUtils.splitCommaStringToArray(rs.getString("roles"));
            List<UserRoleType> roleTypes =  Arrays.asList(roles).stream().map(r -> UserRoleType.valueOf(r)).collect(Collectors.toList());

            UserRoles userRoles = new UserRoles();
            userRoles.setUserId(userId);
            userRoles.setRoles(roleTypes);
            return userRoles;
        }
    }




}
