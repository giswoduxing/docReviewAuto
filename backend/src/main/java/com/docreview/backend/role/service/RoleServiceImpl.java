package com.docreview.backend.role.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.docreview.backend.auth.api.PermissionOptionResponse;
import com.docreview.backend.auth.service.PermissionCatalog;
import com.docreview.backend.common.exception.BusinessException;
import com.docreview.backend.role.api.CreateRoleRequest;
import com.docreview.backend.role.api.RoleSummary;
import com.docreview.backend.role.domain.RoleEntity;
import com.docreview.backend.role.domain.RolePermissionEntity;
import com.docreview.backend.role.mapper.RoleMapper;
import com.docreview.backend.role.mapper.RolePermissionMapper;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl implements RoleService {

    private static final String USER_STATUS_ACTIVE = "ACTIVE";

    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;

    public RoleServiceImpl(RoleMapper roleMapper, RolePermissionMapper rolePermissionMapper) {
        this.roleMapper = roleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Override
    public List<RoleSummary> listRoles() {
        List<RoleEntity> roles = roleMapper.selectList(
            Wrappers.<RoleEntity>lambdaQuery().orderByAsc(RoleEntity::getCode)
        );
        if (roles.isEmpty()) {
            return List.of();
        }

        Map<Long, List<String>> permissionCodesByRoleId = rolePermissionMapper.selectList(
            Wrappers.<RolePermissionEntity>lambdaQuery().in(
                RolePermissionEntity::getRoleId,
                roles.stream().map(RoleEntity::getId).toList()
            )
        ).stream().collect(Collectors.groupingBy(
            RolePermissionEntity::getRoleId,
            Collectors.mapping(RolePermissionEntity::getPermissionCode, Collectors.toList())
        ));

        return roles.stream()
            .map(role -> new RoleSummary(
                role.getId(),
                role.getCode(),
                role.getName(),
                role.getDescription(),
                role.getStatus(),
                role.getBuiltIn() != null && role.getBuiltIn() == 1,
                permissionCodesByRoleId.getOrDefault(role.getId(), List.of())
            ))
            .toList();
    }

    @Override
    public List<PermissionOptionResponse> listPermissionOptions() {
        return PermissionCatalog.all().stream()
            .map(permission -> new PermissionOptionResponse(
                permission.code(),
                permission.titleKey(),
                permission.descriptionKey()
            ))
            .toList();
    }

    @Override
    @Transactional
    public RoleSummary createRole(CreateRoleRequest request) {
        Long existingCount = roleMapper.selectCount(
            Wrappers.<RoleEntity>lambdaQuery().eq(RoleEntity::getCode, request.code().trim())
        );
        if (existingCount != null && existingCount > 0) {
            throw new BusinessException("ROLE_CODE_EXISTS", "role.create.code.exists");
        }

        Set<String> permissionCodes = new LinkedHashSet<>();
        for (String permissionCode : request.permissionCodes()) {
            String normalizedPermission = permissionCode.trim();
            if (!PermissionCatalog.isSupported(normalizedPermission)) {
                throw new BusinessException("ROLE_PERMISSION_INVALID", "role.create.permissions.invalid");
            }
            permissionCodes.add(normalizedPermission);
        }

        RoleEntity role = new RoleEntity();
        role.setCode(request.code().trim());
        role.setName(request.name().trim());
        role.setDescription(blankToNull(request.description()));
        role.setStatus(USER_STATUS_ACTIVE);
        role.setBuiltIn(0);
        roleMapper.insert(role);

        for (String permissionCode : permissionCodes) {
            RolePermissionEntity rolePermission = new RolePermissionEntity();
            rolePermission.setRoleId(role.getId());
            rolePermission.setPermissionCode(permissionCode);
            rolePermissionMapper.insert(rolePermission);
        }

        return new RoleSummary(
            role.getId(),
            role.getCode(),
            role.getName(),
            role.getDescription(),
            role.getStatus(),
            false,
            List.copyOf(permissionCodes)
        );
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
