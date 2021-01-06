package com.javaee.hometask7.alleshop.controllers;

import com.javaee.hometask7.alleshop.models.*;
import com.javaee.hometask7.alleshop.services.ShopItemService;
import com.javaee.hometask7.alleshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
public class AdminController {

    private final ShopItemService shopItemService;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(ShopItemService shopItemService, UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.shopItemService = shopItemService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("")
    public String adminHome(Model model) {
        model.addAttribute("shop_items", shopItemService.getAllShopItems());
        model.addAttribute("brands", shopItemService.getAllBrands());
        model.addAttribute("countries", shopItemService.getAllCountries());
        model.addAttribute("categories", shopItemService.getAllCategories());
//        Optional<Cookie> langCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("language")).findFirst();
//        langCookie.ifPresent(cookie -> model.addAttribute("language", cookie.getValue()));
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("payments", shopItemService.getAllPayments());
        model.addAttribute("language", "eng");
        return "admin-home";
    }

    @GetMapping("/details/{id}")
    public String details(Model model,
                          @PathVariable("id") Long id) {
        ShopItem shopItem = shopItemService.getShopItemById(id);
        List<Category> categories = shopItemService.getAllCategories();
        categories.removeAll(shopItem.getCategories());
        model.addAttribute("shop_item", shopItem);
        model.addAttribute("brands", shopItemService.getAllBrands());
        model.addAttribute("categories", categories);
        model.addAttribute("pictures", shopItemService.getAllShopItemPictures(id));
        return "admin-details";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam("name") String name,
                          @RequestParam("description") String description,
                          @RequestParam("price") double price,
                          @RequestParam("amount") int amount,
                          @RequestParam("stars") double stars,
                          @RequestParam("small_picture_url") String smallPictureUrl,
                          @RequestParam("large_picture_url") String largePictureUrl,
                          @RequestParam(name = "is_top", required = false) Boolean onTop,
                          @RequestParam(name = "brand_id") Long brandId) {
        ShopItem shopItem = createShopItem(name, description, price, amount, stars, smallPictureUrl, largePictureUrl, onTop, brandId);
        shopItem.setAddedDate(new Date(new java.util.Date().getTime()));
        System.out.println(shopItemService.addShopItem(shopItem));
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String editItem(@RequestParam("id") Long id,
                           @RequestParam("name") String name,
                           @RequestParam("description") String description,
                           @RequestParam("price") double price,
                           @RequestParam("amount") int amount,
                           @RequestParam("stars") double stars,
                           @RequestParam("small_picture_url") String smallPictureUrl,
                           @RequestParam("large_picture_url") String largePictureUrl,
                           @RequestParam("added_date") Date addedDate,
                           @RequestParam(name = "is_top", required = false) Boolean onTop,
                           @RequestParam("brand_id") Long brandId) {
        ShopItem shopItem = createShopItem(name, description, price, amount, stars, smallPictureUrl, largePictureUrl, onTop, brandId);
        shopItem.setId(id);
        shopItem.setAddedDate(addedDate);
        System.out.println(shopItemService.editShopItem(shopItem));
        return "redirect:/admin/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable("id") Long id) {
        System.out.println(shopItemService.deleteShopItem(shopItemService.getShopItemById(id)));
        return "redirect:/admin";
    }

    @PostMapping("/add_brand")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addBrand(@RequestParam("name") String name,
                           @RequestParam("country_id") Long countryId) {
        Country country = shopItemService.getCountryById(countryId);
        Brand brand = new Brand(null, name, country);
        System.out.println(shopItemService.addBrand(brand));
        return "redirect:/admin";
    }

    @PostMapping("/edit_brand")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String editBrand(@RequestParam("name") String name,
                            @RequestParam("country_id") Long countryId,
                            @RequestParam("id") Long id) {
        Country country = shopItemService.getCountryById(countryId);
        Brand brand = new Brand(id, name, country);
        System.out.println(shopItemService.editBrand(brand));
        return "redirect:/admin";
    }

    @GetMapping("/delete_brand/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteBrand(@PathVariable("id") Long id) {
        System.out.println(shopItemService.deleteBrand(shopItemService.getBrandById(id)));
        return "redirect:/admin";
    }

    @PostMapping("/add_country")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addCountry(@RequestParam("name") String name,
                             @RequestParam("code") String code) {
        Country country = new Country(null, name, code);
        System.out.println(shopItemService.addCountry(country));
        return "redirect:/admin";
    }

    @PostMapping("/edit_country")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String editCountry(@RequestParam("name") String name,
                              @RequestParam("code") String code,
                              @RequestParam("id") Long id){
        Country country = new Country(id, name, code);
        System.out.println(shopItemService.editCountry(country));
        return "redirect:/admin";
    }

    @GetMapping("/delete_country/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteCountry(@PathVariable("id") Long id) {
        System.out.println(shopItemService.deleteCountry(shopItemService.getCountryById(id)));
        return "redirect:/admin";
    }

    @PostMapping("/add_category")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addCategory(@RequestParam("name") String name,
                              @RequestParam("logo_url") String logoUrl) {
        Category category = new Category(null, name, logoUrl);
        System.out.println(shopItemService.addCategory(category));
        return "redirect:/admin";
    }

    @PostMapping("/edit_category")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String editCategory(@RequestParam("id") Long id,
                               @RequestParam("name") String name,
                               @RequestParam("logo_url") String logoUrl) {
        Category category = new Category(id, name, logoUrl);
        System.out.println(shopItemService.editCategory(category));
        return "redirect:/admin";
    }

    @GetMapping("/delete_category/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteCategory(@PathVariable("id") Long id) {
        System.out.println(shopItemService.deleteCategory(shopItemService.getCategoryById(id)));
        return "redirect:/admin";
    }

    @PostMapping("assign_category_to_item")
    public String assignCategoryToItem(@RequestParam("shop_item_id") Long shopItemId,
                                         @RequestParam("category_id") Long categoryId) {
        Category category = shopItemService.getCategoryById(categoryId);
        if (category != null) {
            ShopItem shopItem = shopItemService.getShopItemById(shopItemId);
            if (shopItem != null) {
                if (shopItem.getCategories() == null) {
                    shopItem.setCategories(new ArrayList<>());
                }
                if (!shopItem.getCategories().contains(category)) {
                    shopItem.getCategories().add(category);
                }
                System.out.println(shopItemService.editShopItem(shopItem));
            }
        }
        return "redirect:/admin/details/" + shopItemId;
    }

    @PostMapping("remove_category_from_item")
    public String removeCategoryFromItem(@RequestParam("shop_item_id") Long shopItemId,
                                         @RequestParam("category_id") Long categoryId) {
        Category category = shopItemService.getCategoryById(categoryId);
        if (category != null) {
            ShopItem shopItem = shopItemService.getShopItemById(shopItemId);
            if (shopItem != null) {
                if (shopItem.getCategories() == null) {
                    shopItem.setCategories(new ArrayList<>());
                }
                shopItem.getCategories().remove(category);
                System.out.println(shopItemService.editShopItem(shopItem));
            }
        }
        return "redirect:/admin/details/" + shopItemId;
    }

    @PostMapping("add_user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addUser(@RequestParam("user_email") String email,
                          @RequestParam("user_full_name") String fullName,
                          @RequestParam("user_password") String password) {
        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPassword(password);

        if (userService.createUser(user) != null) {
            return "redirect:/admin?success";
        }
        return "redirect:/admin?error";
    }

    @GetMapping("user_details/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String userDetails(Model model,
                              @PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        List<Role> roles = userService.getAllRoles();
        roles.removeAll(user.getRoles());
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "admin-user-details";
    }

    @PostMapping("user_edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String userEdit(@RequestParam("user_id") Long id,
                           @RequestParam("user_email") String email,
                           @RequestParam("user_full_name") String fullName) {
        User user = userService.getUserById(id);
        if (user != null) {
            user.setEmail(email);
            user.setFullName(fullName);
            System.out.println(userService.saveUser(user));
        }
        return "redirect:/admin/user_details/" + id;
    }

    @GetMapping("user_delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String userDelete(@PathVariable("id") Long id) {
        System.out.println(userService.deleteUser(userService.getUserById(id)));
        return "redirect:/admin";
    }

    @PostMapping("reset_user_password")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String resetUserPassword(@RequestParam("user_id") Long id,
                                    @RequestParam("user_password") String password) {
        User user = userService.getUserById(id);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(password));
            System.out.println(userService.saveUser(user));
        }
        return "redirect:/admin/user_details/" + id;
    }

    @PostMapping("assign_role_to_user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String assignRoleToUser(@RequestParam("user_id") Long userId,
                                   @RequestParam("role_id") Long roleId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            Role role = userService.getRoleById(roleId);
            if (role != null) {
                if (user.getRoles() == null) {
                    user.setRoles(new ArrayList<>());
                }
                if (!user.getRoles().contains(role)) {
                    user.getRoles().add(role);
                }
                System.out.println(userService.saveUser(user));
            }
        }
        return "redirect:/admin/user_details/" + userId;
    }

    @PostMapping("remove_role_from_user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String removeRoleFromUser(@RequestParam("user_id") Long userId,
                                     @RequestParam("role_id") Long roleId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            Role role = userService.getRoleById(roleId);
            if (role != null) {
                if (user.getRoles() == null) {
                    user.setRoles(new ArrayList<>());
                }
                user.getRoles().remove(role);
                System.out.println(userService.saveUser(user));
            }
        }
        return "redirect:/admin/user_details/" + userId;
    }

    private ShopItem createShopItem(String name,
                                    String description,
                                    double price,
                                    int amount,
                                    double stars,
                                    String smallPictureUrl,
                                    String largePictureUrl,
                                    Boolean onTop,
                                    Long brandId) {
        if (onTop == null) {
            onTop = false;
        }
        Brand brand = shopItemService.getBrandById(brandId);
        ShopItem shopItem = new ShopItem();
        shopItem.setId(null);
        shopItem.setName(name);
        shopItem.setDescription(description);
        shopItem.setPrice(price);
        shopItem.setAmount(amount);
        shopItem.setStars(stars);
        shopItem.setSmallPictureUrl(smallPictureUrl);
        shopItem.setLargePictureUrl(largePictureUrl);
        shopItem.setInTopPage(onTop);
        shopItem.setBrand(brand);
        return shopItem;
    }
}
