package com.docreview.backend.organization.service;

import com.docreview.backend.organization.api.CreateOrganizationRequest;
import com.docreview.backend.organization.api.OrganizationSummary;
import java.util.List;

public interface OrganizationService {

    List<OrganizationSummary> listOrganizations();

    OrganizationSummary createOrganization(CreateOrganizationRequest request);
}
