package com.pashonokk.genericquery.dto;

import java.util.List;
import java.util.Map;

public class GenericRequestDto {
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String sortDirection = "asc";
    private Map<String, List<String>> filterValues;
}
