package com.indhawk.billable.billablews.controllers;

import com.indhawk.billable.billablews.auth.HasRoles;
import com.indhawk.billable.billablews.requests.TaxCategoryConfigRequest;
import com.indhawk.billable.billablews.services.TaxCategoryService;
import com.indhawk.billable.billablews.utils.SecurityContextHelper;
import com.indhawk.billable.models.TaxCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/taxcategories")
public class TaxCategoryController {

    @Autowired
    private TaxCategoryService taxCategoryService;

    @PostMapping(produces = "application/json")
    @HasRoles(roles = {"SUPER_USER", "ADMIN_USER"})
    public boolean saveTaxCategory(@RequestBody TaxCategoryConfigRequest config) {
        int orgId = SecurityContextHelper.getCurrentUserOrgId();
        return taxCategoryService.saveTaxCategory(orgId, config);
    }

    @GetMapping(value = "/{orgId}", produces = "application/json")
    public List<TaxCategory> getAllTaxCategoriesByOrgId(@PathVariable("orgId") int orgId) {
        return taxCategoryService.getTaxCategoriesByOrgId(orgId);
    }

    @GetMapping(value = "/{taxCategoryId}", produces = "application/json")
    public TaxCategory getTaxCategoryByOrgId(@PathVariable("taxCategoryId") int taxCategoryId) {
        int orgId = SecurityContextHelper.getCurrentUserOrgId();
        return taxCategoryService.getTaxCategoryById(orgId, taxCategoryId);
    }




}
