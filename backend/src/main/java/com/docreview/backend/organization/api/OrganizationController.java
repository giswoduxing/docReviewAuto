package com.docreview.backend.organization.api;

import com.docreview.backend.common.api.ApiResponse;
import com.docreview.backend.common.i18n.MessageResolver;
import com.docreview.backend.organization.service.OrganizationService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/internal/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final MessageResolver messageResolver;

    public OrganizationController(OrganizationService organizationService, MessageResolver messageResolver) {
        this.organizationService = organizationService;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('organization:manage', 'user:manage')")
    public ApiResponse<List<OrganizationSummary>> listOrganizations() {
        return ApiResponse.success(
            messageResolver.getMessage("organization.list.success"),
            organizationService.listOrganizations()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('organization:manage')")
    public ApiResponse<OrganizationSummary> createOrganization(@Valid @RequestBody CreateOrganizationRequest request) {
        return ApiResponse.success(
            messageResolver.getMessage("organization.create.success"),
            organizationService.createOrganization(request)
        );
    }
}
