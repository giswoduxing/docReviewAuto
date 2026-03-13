package com.docreview.backend.organization.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.docreview.backend.user.domain.BaseEntity;

@TableName("sys_organization")
public class OrganizationEntity extends BaseEntity {

    @TableId
    private Long id;

    private String name;

    private String code;

    @TableField("parent_id")
    private Long parentId;

    @TableField("leader_name")
    private String leaderName;

    private String status;

    @TableField("built_in")
    private Integer builtIn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBuiltIn() {
        return builtIn;
    }

    public void setBuiltIn(Integer builtIn) {
        this.builtIn = builtIn;
    }
}
