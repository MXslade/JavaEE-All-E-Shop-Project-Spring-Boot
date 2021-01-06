package com.javaee.hometask7.alleshop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopItemSearchParams {

    private String name;
    private String priceFrom;
    private String priceTo;
    private String brandName;

}
