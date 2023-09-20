package com.indhawk.billable.billablews.requests;

import com.indhawk.billable.models.Address;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrganizationConfigRequest {
    private String orgName;
    private Address address;
    private String gstin;
    private String logo;
}
