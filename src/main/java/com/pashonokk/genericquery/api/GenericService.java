package com.pashonokk.genericquery.api;

import com.pashonokk.genericquery.dto.GenericRequestDto;
import com.pashonokk.genericquery.dto.PageResponse;
import com.pashonokk.genericquery.mapper.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GenericService {
    private final PageMapper pageMapper;

    @Transactional(readOnly = true)
    public <T, R> PageResponse<T> fetch(GenericRequestDto dto, JpaSpecificationExecutor<R> repository, Function<R, T> function) {
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), getSort(dto));
        GenericSpecification<R> spec = new GenericSpecification<>(new SearchCriteria(dto.getFilterValues()));
        Page<R> page = repository.findAll(spec, pageable);
        return pageMapper.toPageResponse(page.map(function));
    }

    private static Sort getSort(GenericRequestDto dto) {
        return Sort.by(dto
                .getSort()
                .entrySet()
                .stream()
                .map(entry -> entry.getValue().equalsIgnoreCase("desc") ?
                        Sort.Order.desc(entry.getKey()) :
                        Sort.Order.asc(entry.getKey()))
                .toList());
    }
}
