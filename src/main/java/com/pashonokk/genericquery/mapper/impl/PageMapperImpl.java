package com.pashonokk.genericquery.mapper.impl;


import com.pashonokk.genericquery.dto.PageResponse;
import com.pashonokk.genericquery.mapper.PageMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PageMapperImpl implements PageMapper {
    @Override
    public <T> PageResponse<T> toPageResponse(Page<T> page) {
        return new PageResponse<>(page);
    }
}
