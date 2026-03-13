package com.docreview.backend.user.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class UserPageQuery {

    @Min(value = 1, message = "{user.page.number.min}")
    private long pageNumber = 1;

    @Min(value = 1, message = "{user.page.size.min}")
    @Max(value = 100, message = "{user.page.size.max}")
    private long pageSize = 20;

    public long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }
}
