package com.pashonokk.genericquery.controller;

import com.pashonokk.genericquery.dto.ClientResponseDto;
import com.pashonokk.genericquery.dto.GenericRequestDto;
import com.pashonokk.genericquery.dto.PageResponse;
import com.pashonokk.genericquery.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("clients")
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/apiTest")
    public PageResponse<ClientResponseDto> findAll(@RequestBody(required = false) GenericRequestDto genericRequestDto) {
        return clientService.getAllClientsWithOneFilter(genericRequestDto);
    }

}