package com.indhawk.billable.billablews.calculations;

import com.indhawk.billable.billablews.dao.InvoiceDao;
import com.indhawk.billable.models.AdditionalItem;
import com.indhawk.billable.models.Billable;
import com.indhawk.billable.models.BillableItem;
import com.indhawk.billable.models.Invoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InvoiceCalculator {

    @Autowired
    private InvoiceDao invoiceDao;

    public Invoice calculateInvoice(Invoice invoice) {
        Billable billable = invoice.getBillable();
        Billable calculatedBillable = calculateBillable(billable);
        Invoice calcBillableInvoice = invoice.toBuilder().billable(calculatedBillable).build();
        Invoice calculatedInvoice = calcTotals(calcBillableInvoice, calculatedBillable);
        return calculatedInvoice;
    }

    private Billable calculateBillable(Billable billable) {
        if (billable == null ||
                billable.getBillableItems() == null ||
                 billable.getBillableItems().size() == 0) {
            throw new RuntimeException("Billable Items are null. Nothing to calculate for invoice");
        }
        List<BillableItem> calculatedItems =  billable.getBillableItems().stream().map(item -> {
            return calcBillableItem(item);
        }).collect(Collectors.toList());

        return billable.builder().billableItems(calculatedItems).build();
    }

    private BillableItem  calcBillableItem(BillableItem item) {
        BigDecimal amount = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        BigDecimal sgst = item.getTaxCategory() != null && item.getTaxCategory().getSgst() != null
                ? item.getTaxCategory().getSgst() : BigDecimal.ZERO;
        BigDecimal sgstAmount = amount.multiply(sgst).divide(BigDecimal.valueOf(100));
        BigDecimal cgst = item.getTaxCategory() != null && item.getTaxCategory().getCgst() != null
                ? item.getTaxCategory().getCgst() : BigDecimal.ZERO;
        BigDecimal cgstAmount = amount.multiply(cgst).divide(BigDecimal.valueOf(100));
        BigDecimal igst = item.getTaxCategory() != null && item.getTaxCategory().getIgst() != null
                ? item.getTaxCategory().getIgst() : BigDecimal.ZERO;
        BigDecimal igstAmount = amount.multiply(igst).divide(BigDecimal.valueOf(100));
        BigDecimal cess = item.getTaxCategory() != null && item.getTaxCategory().getCess() != null
                ? item.getTaxCategory().getCess() : BigDecimal.ZERO;
        BigDecimal cessAmount = amount.multiply(cess).divide(BigDecimal.valueOf(100));
        BigDecimal gstAmount = sgstAmount.add(cgstAmount).add(igstAmount);
        BigDecimal taxedAmount = gstAmount.add(cessAmount);
        BigDecimal billableAmount = amount.add(taxedAmount);

        BillableItem billableItem = item.toBuilder().amount(amount)
                .sgstAmount(sgstAmount)
                .cgstAmount(cgstAmount)
                .igstAmount(igstAmount)
                .cessAmount(cessAmount)
                .gstAmount(gstAmount)
                .taxedAmount(taxedAmount)
                .billableAmount(billableAmount)
                .build();
        return billableItem;
    }

    private Invoice calcTotals(Invoice invoice, Billable billable) {
        BigDecimal totalItemsPrice = BigDecimal.ZERO;
        BigDecimal totalSGSTAmount = BigDecimal.ZERO;
        BigDecimal totalCGSTAmount = BigDecimal.ZERO;
        BigDecimal totalIGSTAmount = BigDecimal.ZERO;
        BigDecimal totalCESSAmount = BigDecimal.ZERO;
        BigDecimal totalGSTAmount = BigDecimal.ZERO;
        BigDecimal discountedAmount = BigDecimal.ZERO;
        BigDecimal totalBillableAmount = BigDecimal.ZERO;
        for (BillableItem item : billable.getBillableItems()) {
            totalItemsPrice = totalItemsPrice.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            totalSGSTAmount = totalSGSTAmount.add(item.getSgstAmount());
            totalCGSTAmount = totalCGSTAmount.add(item.getCgstAmount());
            totalIGSTAmount = totalIGSTAmount.add(item.getIgstAmount());
            totalCESSAmount = totalCESSAmount.add(item.getCessAmount());
            totalBillableAmount = totalBillableAmount.add(item.getBillableAmount());
        }
        totalGSTAmount = totalSGSTAmount.add(totalCGSTAmount).add(totalIGSTAmount);

        BigDecimal totalAdditionalAmount = BigDecimal.ZERO;
        if (invoice.getAdditionalDetail() != null && invoice.getAdditionalDetail().getAdditionalItems() != null) {
            for (AdditionalItem charge : invoice.getAdditionalDetail().getAdditionalItems()) {
                totalAdditionalAmount = totalAdditionalAmount.add(charge.getAdditionalAmount());
            }
        }

        totalBillableAmount = totalBillableAmount.add(totalAdditionalAmount);

        return invoice.toBuilder().totalItemsPrice(totalItemsPrice)
                .totalSGSTAmount(totalSGSTAmount)
                .totalCGSTAmount(totalCGSTAmount)
                .totalIGSTAmount(totalIGSTAmount)
                .totalCESSAmount(totalCESSAmount)
                .totalGSTAmount(totalGSTAmount)
                .discountedAmount(discountedAmount)
                .totalAdditionalAmount(totalAdditionalAmount)
                .payableAmount(totalBillableAmount)
                .build();
    }

    private BigDecimal calcDiscount() {
        // TODO
        return null;
    }



}
