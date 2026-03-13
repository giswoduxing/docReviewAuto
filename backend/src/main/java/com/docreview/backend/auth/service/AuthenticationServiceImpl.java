package com.docreview.backend.auth.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.docreview.backend.auth.api.LoginRequest;
import com.docreview.backend.auth.api.SessionUserResponse;
import com.docreview.backend.auth.security.AuthenticatedUserPrincipal;
import com.docreview.backend.common.exception.BusinessException;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String USER_STATUS_ACTIVE = "ACTIVE";
    private static final DateTimeFormatter LOCK_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final SystemUserMapper systemUserMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final OrganizationMapper organizationMapper;
    private final PasswordEncoder passwordEncoder;
    private final FrameworkSecurityProperties securityProperties;
    private final SecurityContextRepository securityContextRepository;

    public AuthenticationServiceImpl(
        SystemUserMapper systemUserMapper,
        UserRoleMapper userRoleMapper,
        RoleMapper roleMapper,
        RolePermissionMapper rolePermissionMapper,
        OrganizationMapper organizationMapper,
        PasswordEncoder passwordEncoder,
        FrameworkSecurityProperties securityProperties,
        SecurityContextRepository securityContextRepository
    ) {
        this.systemUserMapper = systemUserMapper;
        this.userRoleMapper = userRoleMapper;
        this.roleMapper = roleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.organizationMapper = organizationMapper;
        this.passwordEncoder = passwordEncoder;
        this.securityProperties = securityProperties;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    @Transactional(noRollbackFor = BusinessException.class)
    public SessionUserResponse login(
        LoginRequest request,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse
    ) {
        String normalizedUsername = request.username().trim();
        SystemUserEntity user = systemUserMapper.selectOne(
            Wrappers.<SystemUserEntity>lambdaQuery()
                .eq(SystemUserEntity::getUsername, normalizedUsername)
                .last("LIMIT 1")
        );

        if (user == null) {
            throw invalidCredentials();
        }

        ensureUserCanAuthenticate(user);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            handleFailedAuthentication(user);
        }

        clearLockState(user);
        user.setLastLoginAt(LocalDateTime.now());
        systemUserMapper.updateById(user);

        AuthenticatedUserPrincipal principal = buildPrincipal(user);

        HttpSession existingSession = httpServletRequest.getSession(false);
        if (existingSession != null) {
            existingSession.invalidate();
        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(
            new UsernamePasswordAuthenticationToken(principal, null, principal.authorities())
        );
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);

        return toSessionResponse(principal);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
    }

    @Override
    public SessionUserResponse currentSession() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof AuthenticatedUserPrincipal authenticatedUserPrincipal)) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "common.unauthorized");
        }
        return toSessionResponse(authenticatedUserPrincipal);
    }

    private void ensureUserCanAuthenticate(SystemUserEntity user) {
        if (!USER_STATUS_ACTIVE.equals(user.getStatus())) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "ACCOUNT_DISABLED", "auth.login.disabled");
        }

        if (user.getLockedUntil() == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        if (user.getLockedUntil().isAfter(now)) {
            throw accountLocked(user.getLockedUntil());
        }

        clearLockState(user);
        systemUserMapper.updateById(user);
    }

    private void clearLockState(SystemUserEntity user) {
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
    }

    private void handleFailedAuthentication(SystemUserEntity user) {
        int failedAttempts = defaultZero(user.getFailedLoginAttempts()) + 1;
        user.setFailedLoginAttempts(failedAttempts);

        if (failedAttempts >= securityProperties.maxFailedAttempts()) {
            LocalDateTime lockedUntil = LocalDateTime.now().plus(securityProperties.lockDuration());
            user.setLockedUntil(lockedUntil);
            systemUserMapper.updateById(user);
            throw accountLocked(lockedUntil);
        }

        systemUserMapper.updateById(user);
        int remainingAttempts = securityProperties.maxFailedAttempts() - failedAttempts;
        throw new BusinessException(
            HttpStatus.UNAUTHORIZED,
            "INVALID_CREDENTIALS",
            "auth.login.invalid.remaining",
            remainingAttempts
        );
    }

    private BusinessException accountLocked(LocalDateTime lockedUntil) {
        return new BusinessException(
            HttpStatus.LOCKED,
            "ACCOUNT_LOCKED",
            "auth.login.locked",
            LOCK_TIME_FORMATTER.format(lockedUntil)
        );
    }

    private BusinessException invalidCredentials() {
        return new BusinessException(
            HttpStatus.UNAUTHORIZED,
            "INVALID_CREDENTIALS",
            "auth.login.invalid"
        );
    }

    private int defaultZero(Integer value) {
        return value == null ? 0 : value;
    }

    private AuthenticatedUserPrincipal buildPrincipal(SystemUserEntity user) {
        List<UserRoleEntity> userRoles = userRoleMapper.selectList(
            Wrappers.<UserRoleEntity>lambdaQuery().eq(UserRoleEntity::getUserId, user.getId())
        );
        List<Long> roleIds = userRoles.stream().map(UserRoleEntity::getRoleId).distinct().toList();
        List<RoleEntity> roles = roleIds.isEmpty()
            ? List.of()
            : roleMapper.selectBatchIds(roleIds).stream()
                .filter(role -> USER_STATUS_ACTIVE.equals(role.getStatus()))
                .toList();

        Map<Long, List<RolePermissionEntity>> permissionsByRoleId = roles.isEmpty()
            ? Map.of()
            : rolePermissionMapper.selectList(
                Wrappers.<RolePermissionEntity>lambdaQuery().in(RolePermissionEntity::getRoleId, roleIds)
            ).stream().collect(Collectors.groupingBy(RolePermissionEntity::getRoleId));

        Set<String> permissionCodes = new LinkedHashSet<>();
        for (RoleEntity role : roles) {
            permissionsByRoleId.getOrDefault(role.getId(), List.of()).stream()
                .map(RolePermissionEntity::getPermissionCode)
                .forEach(permissionCodes::add);
        }

        return new AuthenticatedUserPrincipal(
            user.getId(),
            user.getUsername(),
            user.getDisplayName(),
            resolveOrganizationName(user.getOrganizationId()),
            roles.stream().map(RoleEntity::getCode).toList(),
            roles.stream().map(RoleEntity::getName).toList(),
            List.copyOf(permissionCodes)
        );
    }

    private String resolveOrganizationName(Long organizationId) {
        if (organizationId == null) {
            return null;
        }

        OrganizationEntity organization = organizationMapper.selectById(organizationId);
        return organization == null ? null : organization.getName();
    }

    private SessionUserResponse toSessionResponse(AuthenticatedUserPrincipal principal) {
        return new SessionUserResponse(
            principal.userId(),
            principal.username(),
            principal.displayName(),
            principal.organizationName(),
            principal.roleCodes(),
            principal.roleNames(),
            principal.permissionCodes()
        );
    }
}
