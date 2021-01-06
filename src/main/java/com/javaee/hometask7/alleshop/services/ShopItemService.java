package com.javaee.hometask7.alleshop.services;

import com.javaee.hometask7.alleshop.models.*;

import java.util.List;

public interface ShopItemService {

    // CRUD for ShopItem
    boolean addShopItem(ShopItem shopItem);

    List<ShopItem> getAllShopItems();

    List<ShopItem> getAllShopItemsInTopPage();

    List<ShopItem> getAllShopItemsNotInTopPage();

    List<ShopItem> getSearchedShopItems(String name, double priceFrom, double priceTo, String brandName);

    List<ShopItem> getSearchedShopItemsOrdered(String name, double priceFrom, double priceTo, String brandName, String order);

    ShopItem getShopItemById(Long id);

    ShopItem findShopItemById(Long id);

    boolean editShopItem(ShopItem shopItem);

    boolean deleteShopItem(ShopItem shopItem);

    // CRUD for Brand
    boolean addBrand(Brand brand);

    List<Brand> getAllBrands();

    Brand getBrandById(Long id);

    boolean editBrand(Brand brand);

    boolean deleteBrand(Brand brand);

    // CRUD for Country
    boolean addCountry(Country country);

    List<Country> getAllCountries();

    Country getCountryById(Long id);

    boolean editCountry(Country country);

    boolean deleteCountry(Country country);

    // CRUD for Category
    boolean addCategory(Category category);

    List<Category> getAllCategories();

    Category getCategoryById(Long id);

    boolean editCategory(Category category);

    boolean deleteCategory(Category category);

    // CRUD for Pictures
    Picture addPicture(Picture picture);

    List<Picture> getAllShopItemPictures(Long shopItemId);

    Picture getPictureById(Long id);

    Picture updatePicture(Picture picture);

    boolean deletePicture(Picture picture);

    // CRUD for Payments
    Payment addPayment(Payment payment);

    List<Payment> getAllPayments();

    // CRUD for Comments
    Comment addComment(Comment comment);

    List<Comment> getAllShopItemComments(Long shopItemId);

    Comment getCommentById(Long commentId);

    Comment updateComment(Comment comment);

    boolean deleteComment(Comment comment);

}
