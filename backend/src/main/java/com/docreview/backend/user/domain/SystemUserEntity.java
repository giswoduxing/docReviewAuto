package com.docreview.backend.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sys_user")
public class SystemUserEntity extends BaseEntity {

    @TableId
    private Long id;

    private String username;

    @TableField("password_hash")
    private String passwordHash;

    @TableField("display_name")
    private String displayName;

    private String email;

    @TableField("organization_id")
    private Long organizationId;

    private String status;

    @TableField("failed_login_attempts")
    private Integer failedLoginAttempts;

    @TableField("locked_until")
    private java.time.LocalDateTime lockedUntil;

    @TableField("last_login_at")
    private java.time.LocalDateTime lastLoginAt;

    @TableField("built_in")
    private Integer builtIn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(Integer failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public java.time.LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(java.time.LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public java.time.LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(java.time.LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Integer getBuiltIn() {
        return builtIn;
    }

    public void setBuiltIn(Integer builtIn) {
        this.builtIn = builtIn;
    }
}
