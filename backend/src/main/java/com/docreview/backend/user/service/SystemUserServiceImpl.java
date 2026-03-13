package com.docreview.backend.user.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.docreview.backend.common.api.PageResult;
import com.docreview.backend.common.exception.BusinessException;
import com.docreview.backend.organization.domain.OrganizationEntity;
import com.docreview.backend.organization.mapper.OrganizationMapper;
import com.docreview.backend.role.domain.RoleEntity;
import com.docreview.backend.role.mapper.RoleMapper;
import com.docreview.backend.user.api.CreateUserRequest;
import com.docreview.backend.user.api.UserPageQuery;
import com.docreview.backend.user.api.UserSummary;
import com.docreview.backend.user.domain.SystemUserEntity;
import com.docreview.backend.user.domain.UserRoleEntity;
import com.docreview.backend.user.mapper.SystemUserMapper;
import com.docreview.backend.user.mapper.UserRoleMapper;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemUserServiceImpl implements SystemUserService {

    private static final String USER_STATUS_ACTIVE = "ACTIVE";

    private final SystemUserMapper systemUserMapper;
    private final OrganizationMapper organizationMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;

    public SystemUserServiceImpl(
        SystemUserMapper systemUserMapper,
        OrganizationMapper organizationMapper,
        RoleMapper roleMapper,
        UserRoleMapper userRoleMapper,
        PasswordEncoder passwordEncoder
    ) {
        this.systemUserMapper = systemUserMapper;
        this.organizationMapper = organizationMapper;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PageResult<UserSummary> listUsers(UserPageQuery query) {
        Page<SystemUserEntity> page = Page.of(query.getPageNumber(), query.getPageSize());
        Page<SystemUserEntity> result = systemUserMapper.selectPage(
            page,
            Wrappers.<SystemUserEntity>lambdaQuery()
                .select(
                    SystemUserEntity::getId,
                    SystemUserEntity::getUsername,
                    SystemUserEntity::getOrganizationId,
                    SystemUserEntity::getDisplayName,
                    SystemUserEntity::getEmail,
                    SystemUserEntity::getStatus,
                    SystemUserEntity::getUpdatedAt
                )
                .orderByDesc(SystemUserEntity::getUpdatedAt)
        );

        Map<Long, String> organizationNameById = loadOrganizations(result.getRecords());
        Map<Long, List<String>> roleNamesByUserId = loadRoleNamesByUserId(result.getRecords());

        List<UserSummary> items = result.getRecords().stream()
            .map(entity -> new UserSummary(
                entity.getId(),
                entity.getUsername(),
                entity.getDisplayName(),
                entity.getEmail(),
                entity.getOrganizationId(),
                organizationNameById.get(entity.getOrganizationId()),
                roleNamesByUserId.getOrDefault(entity.getId(), List.of()),
                entity.getStatus(),
                entity.getUpdatedAt()
            ))
            .toList();

        return new PageResult<>(items, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    @Transactional
    public UserSummary createUser(CreateUserRequest request) {
        List<Long> roleIds = request.roleIds().stream().distinct().toList();
        Long existingCount = systemUserMapper.selectCount(
            Wrappers.<SystemUserEntity>lambdaQuery().eq(SystemUserEntity::getUsername, request.username().trim())
        );
        if (existingCount != null && existingCount > 0) {
            throw new BusinessException("USER_USERNAME_EXISTS", "user.create.username.exists");
        }

        OrganizationEntity organization = organizationMapper.selectById(request.organizationId());
        if (organization == null) {
            throw new BusinessException("USER_ORGANIZATION_NOT_FOUND", "user.create.organization.notFound");
        }

        List<RoleEntity> roles = roleMapper.selectBatchIds(roleIds);
        if (roles.size() != roleIds.size()) {
            throw new BusinessException("USER_ROLE_NOT_FOUND", "user.create.roles.notFound");
        }

        SystemUserEntity user = new SystemUserEntity();
        user.setUsername(request.username().trim());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setDisplayName(request.displayName().trim());
        user.setEmail(request.email().trim());
        user.setOrganizationId(request.organizationId());
        user.setStatus(USER_STATUS_ACTIVE);
        user.setFailedLoginAttempts(0);
        user.setBuiltIn(0);
        systemUserMapper.insert(user);

        for (Long roleId : roleIds) {
            UserRoleEntity userRole = new UserRoleEntity();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }

        return new UserSummary(
            user.getId(),
            user.getUsername(),
            user.getDisplayName(),
            user.getEmail(),
            user.getOrganizationId(),
            organization.getName(),
            roles.stream().map(RoleEntity::getName).toList(),
            user.getStatus(),
            user.getUpdatedAt()
        );
    }

    private Map<Long, String> loadOrganizations(List<SystemUserEntity> users) {
        List<Long> organizationIds = users.stream()
            .map(SystemUserEntity::getOrganizationId)
            .filter(java.util.Objects::nonNull)
            .distinct()
            .toList();
        if (organizationIds.isEmpty()) {
            return Map.of();
        }

        return organizationMapper.selectBatchIds(organizationIds).stream()
            .collect(Collectors.toMap(OrganizationEntity::getId, OrganizationEntity::getName));
    }

    private Map<Long, List<String>> loadRoleNamesByUserId(List<SystemUserEntity> users) {
        List<Long> userIds = users.stream().map(SystemUserEntity::getId).toList();
        if (userIds.isEmpty()) {
            return Map.of();
        }

        List<UserRoleEntity> userRoles = userRoleMapper.selectList(
            Wrappers.<UserRoleEntity>lambdaQuery().in(UserRoleEntity::getUserId, userIds)
        );
        Set<Long> roleIds = userRoles.stream().map(UserRoleEntity::getRoleId).collect(Collectors.toSet());
        Map<Long, RoleEntity> roleById = roleIds.isEmpty()
            ? Map.of()
            : roleMapper.selectBatchIds(roleIds).stream()
                .collect(Collectors.toMap(RoleEntity::getId, role -> role));

        return userRoles.stream().collect(Collectors.groupingBy(
            UserRoleEntity::getUserId,
            Collectors.mapping(userRole -> {
                RoleEntity role = roleById.get(userRole.getRoleId());
                return role == null ? null : role.getName();
            }, Collectors.filtering(java.util.Objects::nonNull, Collectors.toList()))
        ));
    }
}
