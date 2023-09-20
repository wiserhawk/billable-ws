package com.indhawk.billable.billablews.requests;

import lombok.*;

@Data
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserConfigRequest {
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
}
