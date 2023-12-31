package com.pashonokk.genericquery.service;

import com.github.javafaker.Faker;
import com.pashonokk.genericquery.api.GenericService;
import com.pashonokk.genericquery.dto.ClientResponseDto;
import com.pashonokk.genericquery.dto.GenericRequestDto;
import com.pashonokk.genericquery.dto.PageResponse;
import com.pashonokk.genericquery.entity.Bank;
import com.pashonokk.genericquery.entity.Client;
import com.pashonokk.genericquery.entity.Transaction;
import com.pashonokk.genericquery.mapper.ClientMapper;
import com.pashonokk.genericquery.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final Faker faker;
    private final ClientMapper clientMapper;
    private final GenericService genericService;

    @Transactional(readOnly = true)
    public PageResponse<ClientResponseDto> getAllClientsWithOneFilter(GenericRequestDto genericRequestDto) {
        return genericService.fetch(genericRequestDto, clientRepository, clientMapper::toDto);
    }

    @Transactional
    public void saveClients() {
        Client client = Client.builder()
                .lastName(faker.name().lastName())
                .firstName(faker.name().firstName())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .build();

        Bank bank = Bank.builder()
                .address(faker.address().fullAddress())
                .name(faker.company().name())
                .build();

        Transaction transaction1 = Transaction.builder()
                .amount(new Random().nextLong())
                .description(faker.finance().creditCard())
                .transactionDate(OffsetDateTime.now())
                .build();
        Transaction transaction2 = Transaction.builder()
                .amount(new Random().nextLong())
                .description(faker.finance().creditCard())
                .transactionDate(OffsetDateTime.now())
                .build();

        client.setBank(bank);
        transaction1.setClient(client);
        transaction2.setClient(client);
        client.setTransactions(List.of(transaction1, transaction2));
        clientRepository.save(client);
    }
}
