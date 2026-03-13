package com.docreview.backend.user.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.docreview.backend.common.api.PageResult;
import com.docreview.backend.user.api.UserPageQuery;
import com.docreview.backend.user.api.UserSummary;
import com.docreview.backend.user.domain.SystemUserEntity;
import com.docreview.backend.user.mapper.SystemUserMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SystemUserServiceImpl implements SystemUserService {

    private final SystemUserMapper systemUserMapper;

    public SystemUserServiceImpl(SystemUserMapper systemUserMapper) {
        this.systemUserMapper = systemUserMapper;
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
                    SystemUserEntity::getDisplayName,
                    SystemUserEntity::getEmail,
                    SystemUserEntity::getStatus,
                    SystemUserEntity::getUpdatedAt
                )
                .orderByDesc(SystemUserEntity::getUpdatedAt)
        );

        List<UserSummary> items = result.getRecords().stream()
            .map(entity -> new UserSummary(
                entity.getId(),
                entity.getUsername(),
                entity.getDisplayName(),
                entity.getEmail(),
                entity.getStatus(),
                entity.getUpdatedAt()
            ))
            .toList();

        return new PageResult<>(items, result.getTotal(), result.getCurrent(), result.getSize());
    }
}
