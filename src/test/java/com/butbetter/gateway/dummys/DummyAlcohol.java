package com.butbetter.gateway.dummys;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class DummyAlcohol implements Serializable {
    private UUID uuid;
    private AlcoholBeverageType alcoholBeverageType;
    private String name;
    private BigDecimal price;
    private double percentage;
    private double amount;
    private AlcoholRatingType productRatingEnum;
    private int ageOfRestrictions;
    private boolean fairTrade;
    private boolean bio;
    private String countryOfOrigin;

    public DummyAlcohol(AlcoholBeverageType alcoholBeverageType, String name, BigDecimal price, double percentage, double amount, AlcoholRatingType productRatingEnum, int ageOfRestrictions, boolean fairTrade, boolean bio, String countryOfOrigin) {
        this.alcoholBeverageType = alcoholBeverageType;
        this.name = name;
        this.price = price;
        this.percentage = percentage;
        this.amount = amount;
        this.productRatingEnum = productRatingEnum;
        this.ageOfRestrictions = ageOfRestrictions;
        this.fairTrade = fairTrade;
        this.bio = bio;
        this.countryOfOrigin = countryOfOrigin;
    }
}
