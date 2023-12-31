package com.pashonokk.genericquery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pashonokk.genericquery.enumeration.ClientType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    @JsonIgnore
    private List<Transaction> transactions = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private Bank bank;
}
