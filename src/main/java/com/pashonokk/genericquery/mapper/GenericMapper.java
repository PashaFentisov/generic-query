package com.pashonokk.genericquery.mapper;

public interface GenericMapper<T, R> {
    R toEntity(T dto);

    T toDto(R entity);
}
