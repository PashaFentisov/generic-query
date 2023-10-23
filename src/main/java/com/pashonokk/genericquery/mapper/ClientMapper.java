package com.pashonokk.genericquery.mapper;

import com.pashonokk.genericquery.dto.ClientResponseDto;
import com.pashonokk.genericquery.entity.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BankMapper.class)
public interface ClientMapper extends GenericMapper<ClientResponseDto, Client> {
    Client toEntity(ClientResponseDto clientResponseDto);

    ClientResponseDto toDto(Client client);
}
