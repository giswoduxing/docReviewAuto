package com.docreview.backend.auth.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.docreview.backend.config.FrameworkSecurityProperties;
import com.docreview.backend.organization.domain.OrganizationEntity;
import com.docreview.backend.organization.mapper.OrganizationMapper;
import com.docreview.backend.role.domain.RoleEntity;
import com.docreview.backend.role.domain.RolePermissionEntity;
import com.docreview.backend.role.mapper.RoleMapper;
import com.docreview.backend.role.mapper.RolePermissionMapper;
import com.docreview.backend.user.domain.SystemUserEntity;
import com.docreview.backend.user.domain.UserRoleEntity;
import com.docreview.backend.user.mapper.SystemUserMapper;
import com.docreview.backend.user.mapper.UserRoleMapper;
import javax.sql.DataSource;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.Transactional;

@Component
@DependsOnDatabaseInitialization
public class BootstrapDataInitializer implements ApplicationRunner {

    private static final String USER_STATUS_ACTIVE = "ACTIVE";

    private final OrganizationMapper organizationMapper;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final SystemUserMapper systemUserMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final FrameworkSecurityProperties securityProperties;
    private final DataSource dataSource;

    public BootstrapDataInitializer(
        OrganizationMapper organizationMapper,
        RoleMapper roleMapper,
        RolePermissionMapper rolePermissionMapper,
        SystemUserMapper systemUserMapper,
        UserRoleMapper userRoleMapper,
        PasswordEncoder passwordEncoder,
        FrameworkSecurityProperties securityProperties,
        DataSource dataSource
    ) {
        this.organizationMapper = organizationMapper;
        this.roleMapper = roleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.systemUserMapper = systemUserMapper;
        this.userRoleMapper = userRoleMapper;
        this.passwordEncoder = passwordEncoder;
        this.securityProperties = securityProperties;
        this.dataSource = dataSource;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        ResourceDatabasePopulator databasePopulator =
            new ResourceDatabasePopulator(new ClassPathResource("db/schema.sql"));
        databasePopulator.execute(dataSource);
        OrganizationEntity rootOrganization = ensureRootOrganization();
        RoleEntity adminRole = ensureAdminRole();
        ensureAdminPermissions(adminRole);
        SystemUserEntity adminUser = ensureAdminUser(rootOrganization.getId());
        ensureAdminRoleAssignment(adminUser.getId(), adminRole.getId());
    }

    private OrganizationEntity ensureRootOrganization() {
        OrganizationEntity existing = organizationMapper.selectOne(
            Wrappers.<OrganizationEntity>lambdaQuery()
                .eq(OrganizationEntity::getCode, "ROOT")
                .last("LIMIT 1")
        );

        if (existing == null) {
            OrganizationEntity organization = new OrganizationEntity();
            organization.setName("Platform Headquarters");
            organization.setCode("ROOT");
            organization.setStatus(USER_STATUS_ACTIVE);
            organization.setBuiltIn(1);
            organizationMapper.insert(organization);
            return organization;
        }

        existing.setName("Platform Headquarters");
        existing.setStatus(USER_STATUS_ACTIVE);
        existing.setBuiltIn(1);
        organizationMapper.updateById(existing);
        return existing;
    }

    private RoleEntity ensureAdminRole() {
        RoleEntity existing = roleMapper.selectOne(
            Wrappers.<RoleEntity>lambdaQuery()
                .eq(RoleEntity::getCode, "ADMIN")
                .last("LIMIT 1")
        );

        if (existing == null) {
            RoleEntity role = new RoleEntity();
            role.setCode("ADMIN");
            role.setName("Administrator");
            role.setDescription("Built-in platform administrator");
            role.setStatus(USER_STATUS_ACTIVE);
            role.setBuiltIn(1);
            roleMapper.insert(role);
            return role;
        }

        existing.setName("Administrator");
        existing.setDescription("Built-in platform administrator");
        existing.setStatus(USER_STATUS_ACTIVE);
        existing.setBuiltIn(1);
        roleMapper.updateById(existing);
        return existing;
    }

    private void ensureAdminPermissions(RoleEntity adminRole) {
        Set<String> existingPermissionCodes = rolePermissionMapper.selectList(
            Wrappers.<RolePermissionEntity>lambdaQuery().eq(RolePermissionEntity::getRoleId, adminRole.getId())
        ).stream().map(RolePermissionEntity::getPermissionCode).collect(Collectors.toSet());

        for (String permissionCode : PermissionCatalog.permissionCodes()) {
            if (existingPermissionCodes.contains(permissionCode)) {
                continue;
            }

            RolePermissionEntity rolePermission = new RolePermissionEntity();
            rolePermission.setRoleId(adminRole.getId());
            rolePermission.setPermissionCode(permissionCode);
            rolePermissionMapper.insert(rolePermission);
        }
    }

    private SystemUserEntity ensureAdminUser(Long organizationId) {
        SystemUserEntity existing = systemUserMapper.selectOne(
            Wrappers.<SystemUserEntity>lambdaQuery()
                .eq(SystemUserEntity::getUsername, securityProperties.adminUsername())
                .last("LIMIT 1")
        );

        if (existing == null) {
            SystemUserEntity user = new SystemUserEntity();
            user.setUsername(securityProperties.adminUsername());
            user.setPasswordHash(passwordEncoder.encode(securityProperties.adminPassword()));
            user.setDisplayName("Platform Admin");
            user.setEmail("admin@docreview.local");
            user.setOrganizationId(organizationId);
            user.setStatus(USER_STATUS_ACTIVE);
            user.setFailedLoginAttempts(0);
            user.setBuiltIn(1);
            systemUserMapper.insert(user);
            return user;
        }

        existing.setPasswordHash(passwordEncoder.encode(securityProperties.adminPassword()));
        existing.setDisplayName("Platform Admin");
        existing.setEmail("admin@docreview.local");
        existing.setOrganizationId(organizationId);
        existing.setStatus(USER_STATUS_ACTIVE);
        existing.setFailedLoginAttempts(0);
        existing.setLockedUntil(null);
        existing.setBuiltIn(1);
        systemUserMapper.updateById(existing);
        return existing;
    }

    private void ensureAdminRoleAssignment(Long userId, Long roleId) {
        Long existingCount = userRoleMapper.selectCount(
            Wrappers.<UserRoleEntity>lambdaQuery()
                .eq(UserRoleEntity::getUserId, userId)
                .eq(UserRoleEntity::getRoleId, roleId)
        );
        if (existingCount != null && existingCount > 0) {
            return;
        }

        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRoleMapper.insert(userRole);
    }
}
