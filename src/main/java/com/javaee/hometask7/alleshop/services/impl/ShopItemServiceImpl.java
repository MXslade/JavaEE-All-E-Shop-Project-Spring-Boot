package com.javaee.hometask7.alleshop.services.impl;

import com.javaee.hometask7.alleshop.models.*;
import com.javaee.hometask7.alleshop.repositories.*;
import com.javaee.hometask7.alleshop.services.ShopItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopItemServiceImpl implements ShopItemService {

    private final ShopItemRepository shopItemRepository;
    private final BrandRepository brandRepository;
    private final CountryRepository countryRepository;
    private final CategoryRepository categoryRepository;
    private final PictureRepository pictureRepository;
    private final PaymentRepository paymentRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ShopItemServiceImpl(ShopItemRepository shopItemRepository,
                               BrandRepository brandRepository,
                               CountryRepository countryRepository,
                               CategoryRepository categoryRepository,
                               PictureRepository pictureRepository,
                               PaymentRepository paymentRepository,
                               CommentRepository commentRepository) {
        this.shopItemRepository = shopItemRepository;
        this.brandRepository = brandRepository;
        this.countryRepository = countryRepository;
        this.categoryRepository = categoryRepository;
        this.pictureRepository = pictureRepository;
        this.paymentRepository = paymentRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public boolean addShopItem(ShopItem shopItem) {
        shopItemRepository.save(shopItem);
        return true;
    }

    @Override
    public List<ShopItem> getAllShopItems() {
        return shopItemRepository.findAll();
    }

    @Override
    public List<ShopItem> getAllShopItemsInTopPage() {
        return shopItemRepository.findAllByInTopPageTrue();
    }

    @Override
    public List<ShopItem> getAllShopItemsNotInTopPage() {
        return shopItemRepository.findAllByInTopPageFalse();
    }

    @Override
    public List<ShopItem> getSearchedShopItems(String name, double priceFrom, double priceTo, String brandName) {
        return shopItemRepository.findAllByNameLikeAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandNameLike("%" + name + "%", priceFrom, priceTo, "%" + brandName + "%");
    }

    @Override
    public List<ShopItem> getSearchedShopItemsOrdered(String name, double priceFrom, double priceTo, String brandName, String order) {
        if (order.equals("Asc")) {
            return shopItemRepository.findAllByNameLikeAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandNameLikeOrderByPriceAsc("%" + name + "%", priceFrom, priceTo, "%" + brandName + "%");
        } else {
            return shopItemRepository.findAllByNameLikeAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandNameLikeOrderByPriceDesc("%" + name + "%", priceFrom, priceTo, "%" + brandName + "%");
        }
    }

    @Override
    public ShopItem getShopItemById(Long id) {
        return shopItemRepository.getOne(id);
    }

    @Override
    public ShopItem findShopItemById(Long id) {
        return shopItemRepository.findById(id).get();
    }

    @Override
    public boolean editShopItem(ShopItem shopItem) {
        shopItemRepository.save(shopItem);
        return true;
    }

    @Override
    public boolean deleteShopItem(ShopItem shopItem) {
        shopItemRepository.delete(shopItem);
        return true;
    }

    @Override
    public boolean addBrand(Brand brand) {
        brandRepository.save(brand);
        return true;
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brand getBrandById(Long id) {
        return brandRepository.getOne(id);
    }

    @Override
    public boolean editBrand(Brand brand) {
        brandRepository.save(brand);
        return true;
    }

    @Override
    public boolean deleteBrand(Brand brand) {
        brandRepository.delete(brand);
        return true;
    }

    @Override
    public boolean addCountry(Country country) {
        countryRepository.save(country);
        return true;
    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Country getCountryById(Long id) {
        return countryRepository.getOne(id);
    }

    @Override
    public boolean editCountry(Country country) {
        countryRepository.save(country);
        return true;
    }

    @Override
    public boolean deleteCountry(Country country) {
        countryRepository.delete(country);
        return true;
    }

    @Override
    public boolean addCategory(Category category) {
        categoryRepository.save(category);
        return true;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.getOne(id);
    }

    @Override
    public boolean editCategory(Category category) {
        categoryRepository.save(category);
        return true;
    }

    @Override
    public boolean deleteCategory(Category category) {
        categoryRepository.delete(category);
        return true;
    }

    @Override
    public Picture addPicture(Picture picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public List<Picture> getAllShopItemPictures(Long shopItemId) {
        return pictureRepository.findAllByShopItemId(shopItemId);
    }

    @Override
    public Picture getPictureById(Long id) {
        return pictureRepository.getOne(id);
    }

    @Override
    public Picture updatePicture(Picture picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public boolean deletePicture(Picture picture) {
        pictureRepository.delete(picture);
        return true;
    }

    @Override
    public Payment addPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getAllShopItemComments(Long shopItemId) {
        return commentRepository.findAllByShopItemId(shopItemId);
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.getOne(commentId);
    }

    @Override
    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public boolean deleteComment(Comment comment) {
        commentRepository.delete(comment);
        return true;
    }
}
