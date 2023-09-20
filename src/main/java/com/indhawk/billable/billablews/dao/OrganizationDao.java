package com.indhawk.billable.billablews.dao;

import com.indhawk.billable.billablews.utils.JsonUtils;
import com.indhawk.billable.models.Address;
import com.indhawk.billable.models.Organization;
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
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class OrganizationDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean saveOrganization(Organization org) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("org_id", org.getOrgId())
                .addValue("org_name", org.getOrgName())
                .addValue("gstin_no", org.getGstin())
                .addValue("org_logo", org.getLogo())
                .addValue("address_json", JsonUtils.objToJson(org.getAddress()));
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SaveOrganization");
        call.execute(params);
        return true;
    }

    public Organization getOrganizationById(int orgId) {
        String function = "SELECT * FROM GetOrganization(?)";
        try {
            return jdbcTemplate.queryForObject(
                    function,
                    new OrganizationDao.OrganizationRowMapper(),
                    new Object[]{orgId});
        } catch (Exception ex) {
            log.error("Error while getting Organization for ordId={}", orgId, ex);
            return null;
        }
    }

    public List<Organization> getAllOrganizations() {
        String function = "SELECT * FROM GetOrganization(?)";
        try {
            return jdbcTemplate.query(
                    function,
                    new OrganizationDao.OrganizationRowMapper(),
                    new Object[]{null});
        } catch (Exception ex) {
            log.error("Error while getting all Organizations", ex);
            return new ArrayList<>();
        }
    }

    public Organization getOrganizationByName(String orgName) {
        String function = "SELECT * FROM GetOrganizationByName(?)";
        try {
            return jdbcTemplate.queryForObject(
                    function,
                    new OrganizationDao.OrganizationRowMapper(),
                    new Object[]{orgName});
        } catch (Exception ex) {
            log.error("Error while getting Organization for orgName={}",orgName, ex);
            return null;
        }
    }

    private class OrganizationRowMapper implements RowMapper<Organization> {

        @Override
        public Organization mapRow(ResultSet rs, int rowNum) throws SQLException {
            Organization org = Organization.builder()
                    .orgId(rs.getInt("orgid"))
                    .orgName(rs.getString("orgname"))
                    .gstin(rs.getString("gstin"))
                    .logo(rs.getString("logo"))
                    .address(JsonUtils.jsonToObj(rs.getString("address"), Address.class))
                    .build();
            return org;
        }
    }
}
