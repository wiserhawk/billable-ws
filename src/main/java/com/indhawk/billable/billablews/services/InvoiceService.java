package com.indhawk.billable.billablews.services;

import com.indhawk.billable.billablews.calculations.InvoiceCalculator;
import com.indhawk.billable.billablews.dao.InvoiceDao;
import com.indhawk.billable.billablews.requests.BillableItems;
import com.indhawk.billable.billablews.requests.InvoiceConfigRequest;
import com.indhawk.billable.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private InvoiceCalculator invoiceCalculator;

    public int createNewInvoice(int orgId, InvoiceConfigRequest invoiceConfigRequest) {
        Invoice invoice = convertToInvoiceObject(orgId, invoiceConfigRequest);
        int invoiceId = invoiceDao.createInvoice(orgId);
        // Set invoice attributes
        invoice.setInvoiceId(invoiceId);
        invoice.setStatus(InvoiceStatus.OPEN);
        // Update invoice
        Invoice calcuatedInvoice = invoiceCalculator.calculateInvoice(invoice);
        invoiceDao.saveInvoice(orgId, calcuatedInvoice);
        return invoiceId;
    }

    public Invoice getInvoiceById(int invoiceId, int orgId) {
        return invoiceDao.getInvoiceById(invoiceId, orgId);
    }

    public List<Invoice> getInvoicesByPeriod(int orgId, LocalDateTime from, LocalDateTime to) {
        List<Invoice> invoices = invoiceDao.getInvoicesByPeriod(orgId, from, to);
        return invoices.stream().filter(inv -> inv != null).sorted((i1, i2) -> {return i2.getInvoiceId() - i1.getInvoiceId();}).collect(Collectors.toList());
    }

    private Invoice convertToInvoiceObject(int orgId, InvoiceConfigRequest invoiceConfigRequest) {
        if (invoiceConfigRequest == null)
            throw new RuntimeException("InvoiceConfig is null");
        Invoice invoice = Invoice.builder()
                .orgId(orgId)
                .invoiceNumber(invoiceConfigRequest.getInvoiceNumber())
                .invoiceCategory(invoiceConfigRequest.getInvoiceCategory())
                .customer(invoiceConfigRequest.getCustomer())
                .shipTo(invoiceConfigRequest.getShipTo())
                .dateTime(LocalDateTime.now())
                .dueDate(invoiceConfigRequest.getDueDate())
                .disclaimer(invoiceConfigRequest.getDisclaimer())
                .discount(invoiceConfigRequest.getDiscount())
                .additionalDetail(invoiceConfigRequest.getAdditionalDetail())
                .customerOrgId(invoiceConfigRequest.getCustomerOrgId())
                .billable(convertToBillable(invoiceConfigRequest.getBillableItems()))
                .build();
        return invoice;
    }

    private Billable convertToBillable(BillableItems billableItems) {
        if (billableItems == null)
            return null;
        List<BillableItem> billables = billableItems.getItems().stream().map(item -> {
            return BillableItem.builder()
                    .itemName(item.getItemName())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .taxCategory(TaxCategory.builder()
                            .taxCategoryId(item.getTaxCategoryId())
                            .sgst(item.getSgstRate())
                            .cgst(item.getCgstRate())
                            .igst(item.getIgstRate())
                            .cess(item.getCessRate())
                            .build())
                    .build();
        }).collect(Collectors.toList());

        return Billable.builder().billableItems(billables).build();
    }

}
