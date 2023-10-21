package com.pashonokk.genericquery.service;

import com.github.javafaker.Faker;
import com.pashonokk.genericquery.api.ClientSpecification;
import com.pashonokk.genericquery.api.SearchCriteria;
import com.pashonokk.genericquery.dto.GenericRequestDto;
import com.pashonokk.genericquery.dto.PageResponse;
import com.pashonokk.genericquery.entity.Bank;
import com.pashonokk.genericquery.entity.Client;
import com.pashonokk.genericquery.entity.Transaction;
import com.pashonokk.genericquery.mapper.PageMapper;
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
    private final PageMapper pageMapper;
    private final ClientRepository clientRepository;
    private final Faker faker;

    @Transactional(readOnly = true)
    public PageResponse<Client> getClients(GenericRequestDto dto) {


        return null;
    }

    @Transactional(readOnly = true)
    public List<Client> getAllClientsWithOneFilter(GenericRequestDto genericRequestDto) {
        if (genericRequestDto == null) {
            return clientRepository.findAll();
        }
        ClientSpecification spec = new ClientSpecification(new SearchCriteria(genericRequestDto.getFilterValues()));
        return clientRepository.findAll(spec);
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
