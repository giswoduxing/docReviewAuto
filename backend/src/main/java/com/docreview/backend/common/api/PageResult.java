package com.docreview.backend.common.api;

import java.util.List;

public record PageResult<T>(
    List<T> items,
    long total,
    long pageNumber,
    long pageSize
) {
}
