package com.indhawk.billable.billablews.requests;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item {
    /** Name of items */
    private String itemName;
    /** Number of items */
    private int quantity;
    /** Price of a single item */
    private BigDecimal price;
    /** Its identifier of Taxable Category.
     * TaxCategory is tax category under which item lies.
     * All taxes will be calculate based on that category tax percentage.
     */
    private int taxCategoryId;
    /** SGST tax percentage */
    private BigDecimal sgstRate;
    /** CGST tax percentage */
    private BigDecimal cgstRate;
    /** IGST tax percentage */
    private BigDecimal igstRate;
    /** CESS tax percentage */
    private BigDecimal cessRate;
}
