package com.javaee.hometask7.alleshop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment_text")
    @Type(type = "text")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    private ShopItem shopItem;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

}
