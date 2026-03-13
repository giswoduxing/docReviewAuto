package com.docreview.backend.organization.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.docreview.backend.common.exception.BusinessException;
import com.docreview.backend.organization.api.CreateOrganizationRequest;
import com.docreview.backend.organization.api.OrganizationSummary;
import com.docreview.backend.organization.domain.OrganizationEntity;
import com.docreview.backend.organization.mapper.OrganizationMapper;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private static final String USER_STATUS_ACTIVE = "ACTIVE";

    private final OrganizationMapper organizationMapper;

    public OrganizationServiceImpl(OrganizationMapper organizationMapper) {
        this.organizationMapper = organizationMapper;
    }

    @Override
    public List<OrganizationSummary> listOrganizations() {
        List<OrganizationEntity> organizations = organizationMapper.selectList(
            Wrappers.<OrganizationEntity>lambdaQuery().orderByAsc(OrganizationEntity::getCode)
        );
        Map<Long, OrganizationEntity> organizationById = new LinkedHashMap<>();
        organizations.forEach(organization -> organizationById.put(organization.getId(), organization));
        return organizations.stream()
            .map(organization -> toSummary(organization, organizationById))
            .toList();
    }

    @Override
    @Transactional
    public OrganizationSummary createOrganization(CreateOrganizationRequest request) {
        Long existingCount = organizationMapper.selectCount(
            Wrappers.<OrganizationEntity>lambdaQuery().eq(OrganizationEntity::getCode, request.code().trim())
        );
        if (existingCount != null && existingCount > 0) {
            throw new BusinessException("ORGANIZATION_CODE_EXISTS", "organization.create.code.exists");
        }

        OrganizationEntity parent = null;
        if (request.parentId() != null) {
            parent = organizationMapper.selectById(request.parentId());
            if (parent == null) {
                throw new BusinessException("ORGANIZATION_PARENT_NOT_FOUND", "organization.create.parent.notFound");
            }
        }

        OrganizationEntity organization = new OrganizationEntity();
        organization.setName(request.name().trim());
        organization.setCode(request.code().trim());
        organization.setParentId(request.parentId());
        organization.setLeaderName(blankToNull(request.leaderName()));
        organization.setStatus(USER_STATUS_ACTIVE);
        organization.setBuiltIn(0);
        organizationMapper.insert(organization);

        return new OrganizationSummary(
            organization.getId(),
            organization.getName(),
            organization.getCode(),
            organization.getParentId(),
            parent == null ? null : parent.getName(),
            organization.getLeaderName(),
            organization.getStatus()
        );
    }

    private OrganizationSummary toSummary(
        OrganizationEntity organization,
        Map<Long, OrganizationEntity> organizationById
    ) {
        OrganizationEntity parent = organizationById.get(organization.getParentId());
        return new OrganizationSummary(
            organization.getId(),
            organization.getName(),
            organization.getCode(),
            organization.getParentId(),
            parent == null ? null : parent.getName(),
            organization.getLeaderName(),
            organization.getStatus()
        );
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
