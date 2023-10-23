package com.pashonokk.genericquery.mapper;

import com.pashonokk.genericquery.dto.BankResponseDto;
import com.pashonokk.genericquery.entity.Bank;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankMapper {
    Bank toEntity(BankResponseDto bankResponseDto);

    BankResponseDto toDto(Bank bank);
}
