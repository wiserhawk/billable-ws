package com.indhawk.billable.billablews.requests;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserRolesConfigRequest {
    private int userId;
    private List<String> roles;
}
