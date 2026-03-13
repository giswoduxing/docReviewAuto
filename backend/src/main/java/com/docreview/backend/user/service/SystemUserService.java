package com.docreview.backend.user.service;

import com.docreview.backend.common.api.PageResult;
import com.docreview.backend.user.api.CreateUserRequest;
import com.docreview.backend.user.api.UserPageQuery;
import com.docreview.backend.user.api.UserSummary;

public interface SystemUserService {

    PageResult<UserSummary> listUsers(UserPageQuery query);

    UserSummary createUser(CreateUserRequest request);
}
