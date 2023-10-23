package com.pashonokk.genericquery.controller;

import com.pashonokk.genericquery.api.GenericService;
import com.pashonokk.genericquery.dto.ClientResponseDto;
import com.pashonokk.genericquery.dto.GenericRequestDto;
import com.pashonokk.genericquery.dto.PageResponse;
import com.pashonokk.genericquery.mapper.ClientMapper;
import com.pashonokk.genericquery.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("clients")
public class ClientController {
    private final GenericService genericService;
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;




    @GetMapping("/apiTest")
    public PageResponse<ClientResponseDto> findAll(@RequestBody(required = false) GenericRequestDto genericRequestDto) {
        return genericService.fetch(genericRequestDto, clientRepository, clientMapper::toDto);
    }

}