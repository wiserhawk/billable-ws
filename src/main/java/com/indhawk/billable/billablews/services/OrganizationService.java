package com.indhawk.billable.billablews.services;

import com.indhawk.billable.billablews.dao.OrganizationDao;
import com.indhawk.billable.billablews.requests.OrganizationConfigRequest;
import com.indhawk.billable.models.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrganizationService {

    @Autowired
    private OrganizationDao organizationDao;

    public Organization saveOrganization(OrganizationConfigRequest orgReq) {
        try {
            organizationDao.saveOrganization(buildOrganizationObject(orgReq));
            return organizationDao.getOrganizationByName(orgReq.getOrgName());
        } catch (Throwable ex) {
            String error = "Error while persisting Organization to database";
            log.error(error, ex);
            throw new RuntimeException(error);
        }
    }

    public Organization getOrganization(int orgId) {
        return organizationDao.getOrganizationById(orgId);
    }

    public List<Organization> getAllOrganizations() {
        return organizationDao.getAllOrganizations();
    }

    private Organization buildOrganizationObject(OrganizationConfigRequest orgReq) {
        return Organization.builder()
                .orgName(orgReq.getOrgName())
                .gstin(orgReq.getGstin())
                .logo(orgReq.getLogo())
                .address(orgReq.getAddress())
                .build();
    }

}
