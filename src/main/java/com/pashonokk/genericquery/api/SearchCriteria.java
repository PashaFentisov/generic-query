package com.pashonokk.genericquery.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class SearchCriteria {
    private List<Criteria> criteriaList;

    public SearchCriteria(Map<String, String> filterValues) {
        this.criteriaList = filterValues.entrySet().stream()
                .map(entry -> new Criteria(entry.getKey(), entry.getValue()))
                .toList();
    }
}
