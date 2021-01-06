package com.javaee.hometask7.alleshop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_full_name")
    private String customerFullName;

    @OneToOne(fetch = FetchType.EAGER)
    private ShopItem shopItem;

    @Column(name = "payment_amount")
    private int amount;

    @Column(name = "payment_date")
    private Date date;
}
