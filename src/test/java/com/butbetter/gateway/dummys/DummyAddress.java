package com.butbetter.gateway.dummys;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class DummyAddress {
    private UUID id;

    private String name;

    private String companyName;

    private String street;

    private String city;

    private String postCode;

    private String country;

    public DummyAddress(String name, String companyName, String street, String city, String postCode, String country) {
        this.name = name;
        this.companyName = companyName;
        this.street = street;
        this.city = city;
        this.postCode = postCode;
        this.country = country;
    }
}
