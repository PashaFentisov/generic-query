package com.pashonokk.genericquery.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Criteria {
    private String key;
    private Object value;
}