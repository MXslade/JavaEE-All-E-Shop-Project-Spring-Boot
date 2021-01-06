package com.javaee.hometask7.alleshop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "brands")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand_name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Country country;

}
