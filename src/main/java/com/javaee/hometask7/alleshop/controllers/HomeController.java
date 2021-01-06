package com.javaee.hometask7.alleshop.controllers;

import com.javaee.hometask7.alleshop.models.*;
import com.javaee.hometask7.alleshop.services.ShopItemService;
import com.javaee.hometask7.alleshop.services.UserDataService;
import com.javaee.hometask7.alleshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private final ShopItemService shopItemService;
    private final UserDataService userDataService;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public HomeController(ShopItemService shopItemService, UserDataService userDataService, UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.shopItemService = shopItemService;
        this.userDataService = userDataService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("")
    public String home(Model model,
                       HttpSession session) {
        model.addAttribute("current_user", userDataService.getUserData());

        List<ShopItem> shopItems = shopItemService.getAllShopItemsInTopPage();
        shopItems.addAll(shopItemService.getAllShopItemsNotInTopPage());
        model.addAttribute("shop_items", shopItems);
        model.addAttribute("categories", shopItemService.getAllCategories());
        //Optional<Cookie> langCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("language")).findFirst();
        model.addAttribute("language", "eng");
        model.addAttribute("brands", shopItemService.getAllBrands());
        if (session.getAttribute("basket") == null) {
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        model.addAttribute("basket", (List<BasketItem>) session.getAttribute("basket"));
        model.addAttribute("number_of_basket_items", countNumberOfBasketItems((List<BasketItem>) session.getAttribute("basket")));
        return "home";
    }

    @GetMapping("details/{id}")
    public String details(Model model,
                          @PathVariable("id") Long id,
                          HttpSession session) {
        model.addAttribute("current_user", userDataService.getUserData());

        model.addAttribute("shop_item", shopItemService.getShopItemById(id));
        model.addAttribute("brands", shopItemService.getAllBrands());
        model.addAttribute("categories", shopItemService.getAllCategories());
        model.addAttribute("pictures", shopItemService.getAllShopItemPictures(id));
        if (session.getAttribute("basket") == null) {
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        model.addAttribute("basket", (List<BasketItem>) session.getAttribute("basket"));
        model.addAttribute("number_of_basket_items", countNumberOfBasketItems((List<BasketItem>) session.getAttribute("basket")));
        model.addAttribute("comments", shopItemService.getAllShopItemComments(id));
        return "details";
    }

    @GetMapping("search")
    public String search(Model model,
                         @RequestParam(value = "name", defaultValue = "") String name,
                         @RequestParam(value = "price_from", defaultValue = "0") double priceFrom,
                         @RequestParam(value = "price_to", defaultValue = "999999999") double priceTo,
                         @RequestParam(value = "brand_name", defaultValue = "") String brandName,
                         @RequestParam(value = "order", required = false) String order,
                         HttpSession session) {
        model.addAttribute("current_user", userDataService.getUserData());

        List<ShopItem> shopItems;
        if (order == null) {
            shopItems = shopItemService.getSearchedShopItems(name, priceFrom, priceTo, brandName);
        } else {
            shopItems = shopItemService.getSearchedShopItemsOrdered(name, priceFrom, priceTo, brandName, order);
        }
        model.addAttribute("shop_items", shopItems);
        model.addAttribute("brands", shopItemService.getAllBrands());
        model.addAttribute("categories", shopItemService.getAllCategories());
        model.addAttribute("search_params", new ShopItemSearchParams(name, priceFrom == 0 ? "" : String.valueOf(priceFrom), priceTo == 999999999 ? "" : String.valueOf(priceTo), brandName));
        if (session.getAttribute("basket") == null) {
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        model.addAttribute("basket", (List<BasketItem>) session.getAttribute("basket"));
        model.addAttribute("number_of_basket_items", countNumberOfBasketItems((List<BasketItem>) session.getAttribute("basket")));
        return "search";
    }

    @GetMapping("403")
    public String accessDenied(Model model) {
        model.addAttribute("current_user", userDataService.getUserData());

        return "403";
    }

    @GetMapping("login")
    public String login(Model model,
                        HttpSession session) {
        model.addAttribute("current_user", userDataService.getUserData());
        if (session.getAttribute("basket") == null) {
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        model.addAttribute("basket", (List<BasketItem>) session.getAttribute("basket"));
        model.addAttribute("number_of_basket_items", countNumberOfBasketItems((List<BasketItem>) session.getAttribute("basket")));
        return "login";
    }

    @GetMapping("register")
    public String register(Model model,
                           HttpSession session) {
        model.addAttribute("current_user", userDataService.getUserData());
        if (session.getAttribute("basket") == null) {
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        model.addAttribute("basket", (List<BasketItem>) session.getAttribute("basket"));
        model.addAttribute("number_of_basket_items", countNumberOfBasketItems((List<BasketItem>) session.getAttribute("basket")));
        return "register";
    }

    @PostMapping("register")
    public String performRegistration(@RequestParam("user_email") String email,
                                      @RequestParam("user_password") String password,
                                      @RequestParam("user_re_password") String rePassword,
                                      @RequestParam("user_full_name") String fullName) {
        if (password.equals(rePassword)) {

            User user = new User();
            user.setEmail(email);
            user.setFullName(fullName);
            user.setPassword(password);

            if (userService.createUser(user) != null) {
                return "redirect:/register?success";
            }
        }
        return "redirect:/register?error";
    }

    @GetMapping("profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model,
                          HttpSession session) {
        model.addAttribute("current_user", userDataService.getUserData());

        model.addAttribute("brands", shopItemService.getAllBrands());
        model.addAttribute("categories", shopItemService.getAllCategories());
        if (session.getAttribute("basket") == null) {
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        model.addAttribute("basket", (List<BasketItem>) session.getAttribute("basket"));
        model.addAttribute("number_of_basket_items", countNumberOfBasketItems((List<BasketItem>) session.getAttribute("basket")));
        return "profile";
    }

    @PostMapping("user_edit")
    @PreAuthorize("isAuthenticated()")
    public String userEdit(@RequestParam("user_id") Long id,
                           @RequestParam("user_email") String email,
                           @RequestParam("user_full_name") String fullName) {
        User user = userService.getUserById(id);
        User currentUser = userDataService.getUserData();
        if (user != null && id.equals(currentUser.getId()) && user.getEmail().equals(email) && user.getEmail().equals(currentUser.getEmail())) {
            user.setFullName(fullName);
            userService.saveUser(user);
            return "redirect:/profile?success";
        }
        return "redirect:/profile?error";
    }

    @PostMapping("update_user_password")
    @PreAuthorize("isAuthenticated()")
    public String updateUserPassword(@RequestParam("user_id") Long id,
                                     @RequestParam("user_old_password") String oldPassword,
                                     @RequestParam("user_new_password") String newPassword,
                                     @RequestParam("user_re_new_password") String reNewPassword) {
        if (newPassword.equals(reNewPassword)) {
            User currentUser = userDataService.getUserData();
            if (currentUser.getId().equals(id)) {
                User user = userService.getUserById(id);
                if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userService.saveUser(user);
                    return "redirect:/profile?success";
                }
            }
        }
        return "redirect:/profile:/profile?error";
    }

    @GetMapping("basket")
    public String basket(Model model,
                         HttpServletRequest request,
                         HttpServletResponse response,
                         HttpSession session) {
        model.addAttribute("current_user", userDataService.getUserData());

        model.addAttribute("brands", shopItemService.getAllBrands());
        model.addAttribute("categories", shopItemService.getAllCategories());

        if (session.getAttribute("basket") == null) {
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        model.addAttribute("basket", (List<BasketItem>) session.getAttribute("basket"));
        model.addAttribute("number_of_basket_items", countNumberOfBasketItems((List<BasketItem>) session.getAttribute("basket")));
        model.addAttribute("price_sum_of_basket", countSumOfBasket((List<BasketItem>) session.getAttribute("basket")));
        return "basket";
    }

    @PostMapping("add_to_basket")
    public String addToBasket(@RequestParam("shop_item_id") Long shopItemId,
                              @RequestParam("page") String page,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              HttpSession session) {
        ShopItem shopItem = shopItemService.getShopItemById(shopItemId);
        if (session.getAttribute("basket") == null) {
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        if (shopItem != null && shopItem.getAmount() > 0) {
            //long numberOfSimilarItemsInBasket = ((List<ShopItem>) session.getAttribute("basket")).stream().filter(item -> item.getId().equals(shopItemId)).count();
            //long numberOfSimilarItemsInBasket = 0;
            List<BasketItem> basket = (List<BasketItem>) session.getAttribute("basket");
            for (BasketItem item : basket) {
                if (item.getShopItem().getId().equals(shopItemId) && item.getAmount() >= shopItem.getAmount()) {
                    if (page.equals("details")) {
                        return "redirect:/details/" + shopItemId;
                    } else {
                        return "redirect:/basket";
                    }
                }
            }
            boolean added = false;
            for (BasketItem item : basket) {
                if (item.getShopItem().getId().equals(shopItemId)) {
                    item.setAmount(item.getAmount() + 1);
                    added = true;
                }
            }
            if (!added) {
                basket.add(new BasketItem(shopItem, 1));
            }
            Cookie [] cookies = request.getCookies();
            for (Cookie c : cookies) {
                if (c.getName().equals("JSESSIONID")) {
                    c.setMaxAge(14 * 24 * 3600);
                    response.addCookie(c);
                }
            }
        }
        if (page.equals("details")) {
            return "redirect:/details/" + shopItemId;
        } else {
            return "redirect:/basket";
        }
    }

    @PostMapping("delete_from_basket")
    public String deleteFromBasket(@RequestParam("shop_item_id") Long shopItemId,
                                   HttpSession session) {
        if (session.getAttribute("basket") != null) {
            List<BasketItem> basket = (List<BasketItem>) session.getAttribute("basket");
            for (BasketItem item : basket) {
                if (item.getShopItem().getId().equals(shopItemId)) {
                    item.setAmount(item.getAmount() - 1);
                    if (item.getAmount() <= 0) {
                        basket.remove(item);
                    }
                    break;
                }
            }
        }
        return "redirect:/basket";
    }

    @PostMapping("check_in")
    public String checkIn(@RequestParam("full_name") String fullName,
                          @RequestParam("card_number") String cardNumber,
                          @RequestParam("expiration") String expiration,
                          @RequestParam("cvv") String cvv,
                          HttpSession session) {
        List<BasketItem> basket = (List<BasketItem>) session.getAttribute("basket");
        for (BasketItem item : basket) {
            ShopItem shopItem = item.getShopItem();
            shopItem.setAmount(shopItem.getAmount() - item.getAmount());
            shopItemService.editShopItem(shopItem);
            Payment payment = new Payment(null, fullName, shopItem, item.getAmount(), new java.sql.Date(new Date().getTime()));
            shopItemService.addPayment(payment);
        }
        session.setAttribute("basket", null);
        return "redirect:/basket";
    }

    @GetMapping("clear_basket")
    public String clearBasket(HttpSession session) {
        if (session.getAttribute("basket") != null) {
            session.setAttribute("basket", null);
        }
        return "redirect:/basket";
    }

    @PostMapping("add_comment_to_item")
    @PreAuthorize("isAuthenticated()")
    public String addCommentToItem(@RequestParam("shop_item_id") Long shopItemId,
                                   @RequestParam("comment_text") String commentText) {
        User currentUser = userDataService.getUserData();
        if (currentUser != null) {
            ShopItem shopItem = shopItemService.getShopItemById(shopItemId);
            if (shopItem != null) {
                Comment comment = new Comment();
                comment.setShopItem(shopItem);
                comment.setText(commentText);
                comment.setUser(currentUser);
                System.out.println(shopItemService.addComment(comment));
            }
        }
        return "redirect:/details/" + shopItemId;
    }

    @GetMapping("delete_comment/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public String deleteComment(@PathVariable("commentId") Long commentId) {
        User currentUser = userDataService.getUserData();
        Comment comment = shopItemService.getCommentById(commentId);
        if ((currentUser != null && comment != null) && (currentUser.getId().equals(comment.getUser().getId()) || isModerator(currentUser) || isAdmin(currentUser))) {
            long shopItemId = comment.getShopItem().getId();
            System.out.println(shopItemService.deleteComment(comment));
            return "redirect:/details/" + shopItemId;
        }
        return "redirect:/403";
    }

    @PostMapping("edit_comment")
    @PreAuthorize("isAuthenticated()")
    public String editComment(@RequestParam("comment_text") String commentText,
                              @RequestParam("comment_id") Long commentId) {
        User currentUser= userDataService.getUserData();
        Comment comment = shopItemService.getCommentById(commentId);
        if (currentUser != null & comment != null && comment.getUser().getId().equals(currentUser.getId())) {
            comment.setText(commentText);
            shopItemService.updateComment(comment);
            return "redirect:/details/" + comment.getShopItem().getId();
        }
        return "redirect:/403";
    }

    private long countNumberOfBasketItems(List<BasketItem> basket) {
        long res = 0;
        for (BasketItem item : basket) {
            res += item.getAmount();
        }
        return res;
    }

    private double countSumOfBasket(List<BasketItem> basket) {
        double res = 0;
        for (BasketItem item : basket) {
            res += item.getAmount() * item.getShopItem().getPrice();
        }
        return res;
    }

    private boolean isModerator(User user) {
        return user.getRoles().contains(userService.getRoleById(3L));
    }

    private boolean isAdmin(User user) {
        return user.getRoles().contains(userService.getRoleById(2L));
    }
}
