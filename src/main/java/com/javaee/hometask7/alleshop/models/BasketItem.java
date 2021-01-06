package com.javaee.hometask7.alleshop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketItem {

    private ShopItem shopItem;
    private int amount;

}
