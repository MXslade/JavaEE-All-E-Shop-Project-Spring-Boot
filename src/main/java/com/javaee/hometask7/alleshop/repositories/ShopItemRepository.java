package com.javaee.hometask7.alleshop.repositories;

import com.javaee.hometask7.alleshop.models.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {

    List<ShopItem> findAllByInTopPageTrue();

    List<ShopItem> findAllByInTopPageFalse();

    List<ShopItem> findAllByNameLikeAndPriceGreaterThanEqualAndPriceLessThanEqual(String name, double priceFrom, double priceTo);

    List<ShopItem> findAllByNameLikeAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceAsc(String name, double priceFrom, double priceTo);

    List<ShopItem> findAllByNameLikeAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceDesc(String name, double priceFrom, double priceTo);

    List<ShopItem> findAllByNameLikeAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandNameLike(String name, double priceFrom, double priceTo, String brandName);

    List<ShopItem> findAllByNameLikeAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandNameLikeOrderByPriceAsc(String name, double priceFrom, double priceTo, String brandName);

    List<ShopItem> findAllByNameLikeAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandNameLikeOrderByPriceDesc(String name, double priceFrom, double priceTo, String brandName);

}
