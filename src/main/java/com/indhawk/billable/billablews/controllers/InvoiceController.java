package com.indhawk.billable.billablews.controllers;

import com.indhawk.billable.billablews.auth.HasRoles;
import com.indhawk.billable.billablews.requests.InvoiceConfigRequest;
import com.indhawk.billable.billablews.services.InvoiceService;
import com.indhawk.billable.billablews.utils.CommonUtils;
import com.indhawk.billable.billablews.utils.SecurityContextHelper;
import com.indhawk.billable.models.Invoice;
import com.indhawk.billable.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping(produces = "application/json")
    @HasRoles(roles = {"SUPER_USER", "ADMIN_USER"})
    public Invoice createInvoice(@RequestBody InvoiceConfigRequest invoiceConfigRequest) {
        User user = SecurityContextHelper.getPrincipal();
        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        int invoiceId = invoiceService.createNewInvoice(user.getOrgId(), invoiceConfigRequest);
        return invoiceService.getInvoiceById(invoiceId, user.getOrgId());
    }

    @GetMapping(value = "/{invoiceId}", produces = "application/json")
    @HasRoles(roles = {"SUPER_USER", "ADMIN_USER"})
    public Invoice getInvoiceById(@PathVariable("invoiceId") int invoiceId) {
        User user = SecurityContextHelper.getPrincipal();
        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        return invoiceService.getInvoiceById(invoiceId, user.getOrgId());
    }

    /**
     *  Returns all invoices for client with in given date range.
     * @param from String date format must be dd-mm-yyyy
     * @param to String date format must be dd-mm-yyyy
     * @return
     */
    @GetMapping(value = "/{from}/{to}")
    @HasRoles(roles = {"SUPER_USER", "ADMIN_USER"})
    public List<Invoice> getInvoicesByPeriod(@PathVariable("from") String from,
                                             @PathVariable("to") String to) {
        User user = SecurityContextHelper.getPrincipal();
        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        return invoiceService.getInvoicesByPeriod(user.getOrgId(), CommonUtils.strToDate(from), CommonUtils.strToDate(to));
    }

}
