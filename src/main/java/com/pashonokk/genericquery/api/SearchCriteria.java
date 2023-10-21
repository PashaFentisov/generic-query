package com.pashonokk.genericquery.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class SearchCriteria {
    private List<Criteria> criteriaList;

    public SearchCriteria(Map<String, String> filterValues) {
        List<Criteria> criteriaList = new ArrayList<>();

        for (Map.Entry<String, String> entry : filterValues.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Criteria criteria = new Criteria(key, value);
            criteriaList.add(criteria);
        }
        this.criteriaList = criteriaList;
    }
}
