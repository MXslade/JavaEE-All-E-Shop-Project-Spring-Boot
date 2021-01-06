package com.javaee.hometask7.alleshop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "pictures")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "picture_url")
    private String url;

    @Column(name = "picture_added_date")
    private Date addedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private ShopItem shopItem;
}
