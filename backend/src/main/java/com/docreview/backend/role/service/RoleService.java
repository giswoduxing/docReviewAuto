package com.docreview.backend.role.service;

import com.docreview.backend.auth.api.PermissionOptionResponse;
import com.docreview.backend.role.api.CreateRoleRequest;
import com.docreview.backend.role.api.RoleSummary;
import java.util.List;

public interface RoleService {

    List<RoleSummary> listRoles();

    List<PermissionOptionResponse> listPermissionOptions();

    RoleSummary createRole(CreateRoleRequest request);
}
