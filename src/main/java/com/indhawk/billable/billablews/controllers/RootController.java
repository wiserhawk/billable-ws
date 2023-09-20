package com.indhawk.billable.billablews.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String home() {
        return "Hello!!! This is Billable worlds best Invoicing Tool";
    }

    @GetMapping("/error")
    public String error() {
        return "Opps!!! Something goes wrong, Billable team will fix it very soon.";
    }
}
