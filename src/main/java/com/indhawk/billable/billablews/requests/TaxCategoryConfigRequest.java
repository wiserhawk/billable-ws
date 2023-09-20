package com.indhawk.billable.billablews.requests;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TaxCategoryConfigRequest {
    private String name;
    private String hsnCode;
    private BigDecimal sgstRate;
    private BigDecimal cgstRate;
    private BigDecimal igstRate;
    private BigDecimal gstRate;
    private BigDecimal cess;
}
