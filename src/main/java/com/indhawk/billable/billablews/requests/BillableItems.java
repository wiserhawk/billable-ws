package com.indhawk.billable.billablews.requests;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BillableItems {
    private List<Item> items;
}
