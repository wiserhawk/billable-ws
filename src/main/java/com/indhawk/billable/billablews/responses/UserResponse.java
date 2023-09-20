package com.indhawk.billable.billablews.responses;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponse {
    private String userId;
    private String name;
    private String surname;
    private String userName;
    private String email;
    private LocalDateTime createdOn;
    private LocalDateTime lastLoginOn;
    private boolean sso;
    private boolean active;
    private int orgId;
}
