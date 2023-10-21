package com.pashonokk.genericquery.mapper;

import com.pashonokk.genericquery.dto.PageResponse;
import org.springframework.data.domain.Page;

public interface PageMapper {
    <T> PageResponse<T> toPageResponse(Page<T> page);
}
