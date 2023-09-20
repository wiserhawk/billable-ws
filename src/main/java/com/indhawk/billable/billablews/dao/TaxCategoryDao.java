package com.indhawk.billable.billablews.dao;

import com.indhawk.billable.models.TaxCategory;
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
public class TaxCategoryDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean saveTaxCategory(int orgId, TaxCategory tax) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("tax_category_id", tax.getTaxCategoryId())
                .addValue("org_id", orgId)
                .addValue("category_name", tax.getName())
                .addValue("hsn_code", tax.getHsnCode())
                .addValue("sgst_rate", tax.getSgst())
                .addValue("cgst_rate", tax.getCgst())
                .addValue("igst_rate", tax.getIgst())
                .addValue("gst_rate", tax.getGst())
                .addValue("cess_rate", tax.getCess());

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SaveTaxCategory");
        call.execute(params);
        return true;
    }

    public TaxCategory getTaxCategoryById(int orgId, int taxCategoryId) {
        String function = "SELECT * FROM GetTaxCategory(?, ?)";
        try {
            return jdbcTemplate.queryForObject(
                    function,
                    new TaxCategoryRowMapper(),
                    new Object[]{orgId, taxCategoryId});
        } catch (Exception ex) {
            log.error("Error while getting Tax Category for ordId={} and taxCategoryId={}",orgId, taxCategoryId, ex);
            return null;
        }
    }

    public List<TaxCategory> getTaxCategoriesByOrgId(int orgId) {
        String function = "SELECT * FROM GetTaxCategory(?, ?)";
        try {
            return jdbcTemplate.query(
                    function,
                    new TaxCategoryRowMapper(),
                    new Object[]{orgId, null});
        } catch (Exception ex) {
            log.error("Error while getting Tax Categories for ordId={}",orgId, ex);
            return new ArrayList<>();
        }
    }

    private class TaxCategoryRowMapper implements RowMapper<TaxCategory> {

        @Override
        public TaxCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
            TaxCategory tax = TaxCategory.builder()
                    .taxCategoryId(rs.getInt("taxcategoryid"))
                    .name(rs.getString("name"))
                    .hsnCode(rs.getString("hsncode"))
                    .sgst(rs.getBigDecimal("sgst"))
                    .cgst(rs.getBigDecimal("cgst"))
                    .igst(rs.getBigDecimal("igst"))
                    .gst(rs.getBigDecimal("gst"))
                    .cess(rs.getBigDecimal("cess"))
                    .orgId(rs.getInt("orgid"))
                    .build();
            return tax;
        }
    }

}
