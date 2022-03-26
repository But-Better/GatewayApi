package com.butbetter.gateway.dummys;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class DummyProductInformation implements Serializable {
    private UUID uuid;

    private String deliveryTime;

    private int amount;

    private DummyAddress address;

    public DummyProductInformation(String deliveryTime, int amount, DummyAddress address) {
        this.deliveryTime = deliveryTime;
        this.amount = amount;
        this.address = address;
    }
}
