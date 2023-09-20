package com.indhawk.billable.billablews.services;

import com.indhawk.billable.billablews.dao.TaxCategoryDao;
import com.indhawk.billable.billablews.requests.TaxCategoryConfigRequest;
import com.indhawk.billable.models.TaxCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TaxCategoryService {

    @Autowired
    private TaxCategoryDao taxCategoryDao;

    public boolean saveTaxCategory(int orgId, TaxCategoryConfigRequest taxCategoryConfig) {
        return taxCategoryDao.saveTaxCategory(orgId, buildTaxCategoryObject(taxCategoryConfig));
    }

    public TaxCategory getTaxCategoryById(int orgId, int taxCategoryId) {
        return taxCategoryDao.getTaxCategoryById(orgId, taxCategoryId);
    }

    public List<TaxCategory> getTaxCategoriesByOrgId(int orgId) {
        return taxCategoryDao.getTaxCategoriesByOrgId(orgId);
    }

    private TaxCategory buildTaxCategoryObject(TaxCategoryConfigRequest config) {
        return TaxCategory.builder().name(config.getName())
                .hsnCode(config.getHsnCode())
                .sgst(config.getSgstRate())
                .cgst(config.getCgstRate())
                .igst(config.getIgstRate())
                .gst(config.getGstRate())
                .cess(config.getCess())
                .build();
    }

}
