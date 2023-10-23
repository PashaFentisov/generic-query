package com.pashonokk.genericquery.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class GenericRequestDto {
    private int page = 0;
    private int size = 10;
    private Map<String, String> sort;
    private Map<String, String> filterValues; //todo тут як валуе має бути список
}
