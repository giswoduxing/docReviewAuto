package com.docreview.backend.role.api;

import com.docreview.backend.auth.api.PermissionOptionResponse;
import com.docreview.backend.common.api.ApiResponse;
import com.docreview.backend.common.i18n.MessageResolver;
import com.docreview.backend.role.service.RoleService;
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
@RequestMapping("/api/internal/roles")
public class RoleController {

    private final RoleService roleService;
    private final MessageResolver messageResolver;

    public RoleController(RoleService roleService, MessageResolver messageResolver) {
        this.roleService = roleService;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('role:manage', 'user:manage')")
    public ApiResponse<List<RoleSummary>> listRoles() {
        return ApiResponse.success(
            messageResolver.getMessage("role.list.success"),
            roleService.listRoles()
        );
    }

    @GetMapping("/permissions")
    @PreAuthorize("hasAuthority('role:manage')")
    public ApiResponse<List<PermissionOptionResponse>> listPermissionOptions() {
        return ApiResponse.success(
            messageResolver.getMessage("role.permissions.success"),
            roleService.listPermissionOptions()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('role:manage')")
    public ApiResponse<RoleSummary> createRole(@Valid @RequestBody CreateRoleRequest request) {
        return ApiResponse.success(
            messageResolver.getMessage("role.create.success"),
            roleService.createRole(request)
        );
    }
}
