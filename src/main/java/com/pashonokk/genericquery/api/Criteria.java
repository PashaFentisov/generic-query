package com.pashonokk.genericquery.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
class Criteria {
    private String key;
    private List<String> values;
}