package com.indhawk.billable.billablews.auth;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface HasRoles {
    String[] roles();
}
