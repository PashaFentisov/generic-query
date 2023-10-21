package com.pashonokk.genericquery.controller;

import com.pashonokk.genericquery.dto.GenericRequestDto;
import com.pashonokk.genericquery.dto.PageResponse;
import com.pashonokk.genericquery.entity.Client;
import com.pashonokk.genericquery.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("clients")
public class ClientController {
    private final ClientService clientService;
    @GetMapping
    public PageResponse<Client> getClients(GenericRequestDto dto){
        return clientService.getClients(dto);
    }

    @GetMapping("/apiTest")
    public List<Client> findAll(@RequestBody(required = false) GenericRequestDto genericRequestDto) {
        return clientService.getAllClientsWithOneFilter(genericRequestDto);
    }

}