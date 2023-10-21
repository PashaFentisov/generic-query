package com.pashonokk.genericquery.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private BankResponseDto bank;
}
