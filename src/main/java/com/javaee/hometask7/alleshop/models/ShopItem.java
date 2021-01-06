package com.javaee.hometask7.alleshop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "shop_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_item_id")
    private Long id;

    @Column(name = "shop_item_name")
    private String name;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "shop_item_description")
    private String description;

    @Column(name = "shop_item_price")
    private double price;

    @Column(name = "shop_item_amount")
    private int amount;

    @Column(name = "shop_item_rating")
    private double stars;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "shop_item_small_picture_url")
    private String smallPictureUrl;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "shop_item_large_picture_url")
    private String largePictureUrl;

    @Column(name = "shop_item_added_date")
    private Date addedDate;

    @Column(name = "shop_item_in_top_page")
    private boolean inTopPage;

    @ManyToOne(fetch = FetchType.EAGER)
    private Brand brand;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Category> categories;

    public int getFullStars() {
        return (int)(stars);
    }

    public int getHalfStars() {
        return (stars - getFullStars() >= 0.5 ? 1 : 0);
    }

    public int getEmptyStars() {
        return 5 - getFullStars() - getHalfStars();
    }

}
