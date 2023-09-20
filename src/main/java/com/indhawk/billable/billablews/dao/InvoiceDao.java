package com.indhawk.billable.billablews.dao;

import com.indhawk.billable.billablews.utils.JsonUtils;
import com.indhawk.billable.models.Invoice;
import com.indhawk.billable.models.InvoiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class InvoiceDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int createInvoice(int orgId) {
        // Create InvoiceId with least information.
        LocalDateTime now = LocalDateTime.now();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("version", 1)
                .addValue("org_id", orgId)
                .addValue("status", InvoiceStatus.OPEN.name())
                .addValue("created_date",now)
                .addValue("last_update", now);
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withProcedureName("CreateInvoiceJson");
        call.execute(params);
        // Get latest created InvoiceId
        return getLatestInvoiceId(orgId);
    }

    public int getLatestInvoiceId(int orgId) {
        String function = "SELECT * FROM GetLatestInvoiceId(?)";
        return jdbcTemplate.queryForObject(
                function,
                (rs, rowNum) -> {
                    return rs.getInt(1);
                },
                new Object[]{orgId});
    }

    public List<Invoice> getInvoicesByPeriod(int orgId, LocalDateTime from, LocalDateTime to) {
        String function = "SELECT * FROM GetInvoicesByPeriod(?, ?, ?)";
        return jdbcTemplate.query(
                function,
                (rs, rowNum) -> {
                    String json = rs.getString("json");
                    if (json != null) {
                        Invoice invoice = JsonUtils.jsonToObj(json, Invoice.class);
                        return invoice;
                    } else {
                        return null;
                    }
                },
                new Object[]{orgId, from, to});
    }

    public boolean saveInvoice(int orgId, Invoice invoice) {
        // Update invoice for given invoiceId
        LocalDateTime now = LocalDateTime.now();
        invoice.setLastUpdateDate(now);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("invoice_id", invoice.getInvoiceId())
                .addValue("invoice_num", invoice.getInvoiceNumber())
                .addValue("version_num", 1)
                .addValue("invoice_name", invoice.getName())
                .addValue("org_id", orgId)
                .addValue("invoice_status", InvoiceStatus.OPEN.name())
                .addValue("invoice_json", JsonUtils.objToJson(invoice))
                .addValue("customer_id", invoice.getCustomerOrgId())
                .addValue("is_deleted", invoice.isDeleted())
                .addValue("last_update", now);
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SaveInvoiceJson");
        call.execute(params);
        return true;
    }

    public Invoice getInvoiceById(int invoiceId, int orgId) {
        String function = "SELECT * FROM GetInvoiceById(?, ?)";
        return jdbcTemplate.queryForObject(
                function,
                (rs, rowNum) -> {
                    String json = rs.getString("json");
                    return JsonUtils.jsonToObj(json, Invoice.class);
                },
                new Object[]{invoiceId, orgId});
    }


}
