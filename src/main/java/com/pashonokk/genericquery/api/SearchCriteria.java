package com.pashonokk.genericquery.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class SearchCriteria {
    private List<Criteria> criteriaList;

    public SearchCriteria(Map<String, String> filterValues) {
        List<Criteria> criteriaList = filterValues.entrySet().stream()
                .map(entry -> new Criteria(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        this.criteriaList = criteriaList;
    }
}
