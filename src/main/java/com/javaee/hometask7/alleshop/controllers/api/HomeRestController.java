package com.javaee.hometask7.alleshop.controllers.api;

import com.javaee.hometask7.alleshop.models.Brand;
import com.javaee.hometask7.alleshop.models.Category;
import com.javaee.hometask7.alleshop.models.Picture;
import com.javaee.hometask7.alleshop.models.ShopItem;
import com.javaee.hometask7.alleshop.services.ShopItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class HomeRestController {

    private final ShopItemService shopItemService;

    @Autowired
    public HomeRestController(ShopItemService shopItemService) {
        this.shopItemService = shopItemService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("items")
    public List<ShopItem> getItems() {
        return shopItemService.getAllShopItems();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("categories")
    public List<Category> getCategories() {
        return shopItemService.getAllCategories();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("brands")
    public List<Brand> getBrands() {
        return shopItemService.getAllBrands();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("details/{id}")
    public ShopItem getShopItemById(@PathVariable("id") Long id) {
        return shopItemService.findShopItemById(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("shop_item_pictures/{shop_item_id}")
    public List<Picture> getShopItemPictures(@PathVariable("shop_item_id") Long shopItemId) {
        return shopItemService.getAllShopItemPictures(shopItemId);
    }
}
