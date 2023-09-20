package com.indhawk.billable.billablews.controllers;

import com.indhawk.billable.billablews.auth.HasRoles;
import com.indhawk.billable.billablews.requests.OrganizationConfigRequest;
import com.indhawk.billable.billablews.services.OrganizationService;
import com.indhawk.billable.billablews.services.UserService;
import com.indhawk.billable.billablews.utils.SecurityContextHelper;
import com.indhawk.billable.models.Organization;
import com.indhawk.billable.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    @PostMapping(produces = "application/json")
    @HasRoles(roles={"SUPER_USER", "ADMIN_USER"})
    public Organization saveOrganization(@RequestBody OrganizationConfigRequest orgReq) {
        Organization org = organizationService.saveOrganization(orgReq);
        User user = SecurityContextHelper.getPrincipal().toBuilder().orgId(org.getOrgId()).build();
        // link organization with current user;
        userService.saveUser(user);
        return org;
    }

    @GetMapping(value = "/{orgId}", produces = "application/json")
    @HasRoles(roles={"SUPER_USER", "ADMIN_USER"})
    public Organization getOrganizationById(@PathVariable("orgId") int orgId) {
        return organizationService.getOrganization(orgId);
    }

    @GetMapping(produces = "application/json")
    @HasRoles(roles={"SUPER_USER"})
    public List<Organization> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }



}

