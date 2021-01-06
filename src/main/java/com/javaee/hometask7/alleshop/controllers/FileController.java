package com.javaee.hometask7.alleshop.controllers;

import com.javaee.hometask7.alleshop.models.Picture;
import com.javaee.hometask7.alleshop.models.ShopItem;
import com.javaee.hometask7.alleshop.models.User;
import com.javaee.hometask7.alleshop.services.ShopItemService;
import com.javaee.hometask7.alleshop.services.UserDataService;
import com.javaee.hometask7.alleshop.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class FileController {

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShopItemService shopItemService;

    @Value("${file.profilePicture.viewPath}")
    private String profilePictureViewPath;

    @Value("${file.profilePicture.uploadPath}")
    private String profilePictureUploadPath;

    @Value("${file.profilePicture.defaultPicture}")
    private String profilePictureDefault;

    @Value("${file.shopItemPicture.viewPath}")
    private String shopItemPictureViewPath;

    @Value("${file.shopItemPicture.uploadPath}")
    private String shopItemPictureUploadPath;

    @Value("${file.shopItemPicture.defaultPicture}")
    private String shopItemPictureDefault;

    @GetMapping(value = "/profile_picture/{url}", produces = {MediaType.IMAGE_JPEG_VALUE})
//    @PreAuthorize("isAuthenticated()")
    public @ResponseBody byte[] getProfilePicture(@PathVariable("url") String url) throws IOException {
        String pictureUrl = profilePictureViewPath + profilePictureDefault;

        if (url!= null && !url.equals("null")) {
            pictureUrl = profilePictureViewPath + url + ".jpg";
        }

        InputStream inputStream;

        try {
            ClassPathResource resource = new ClassPathResource(pictureUrl);
            inputStream = resource.getInputStream();

        } catch (Exception e) {
            ClassPathResource resource = new ClassPathResource(profilePictureViewPath + profilePictureDefault);
            inputStream = resource.getInputStream();
        }

        return IOUtils.toByteArray(inputStream);
    }

    @PostMapping("/update_profile_picture")
    @PreAuthorize("isAuthenticated()")
    public String updateProfilePicture(@RequestParam("profile_picture") MultipartFile picture) {
        if (Objects.equals(picture.getContentType(), "image/jpeg") || Objects.equals(picture.getContentType(), "image/png")) {
            try {
                User user = userDataService.getUserData();

                String pictureName = DigestUtils.sha1Hex("profile_picture_" + user.getId() + "_!Picture");

                byte[] bytes = picture.getBytes();
                Path path = Paths.get(profilePictureUploadPath + pictureName + ".jpg");
                Files.write(path, bytes);

                user.setProfilePicture(pictureName);

                userService.saveUser(user);

                return "redirect:/profile?profile_picture_update_success";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/profile?profile_picture_update_error";
    }

    @GetMapping(value = "/shop_item_picture/{url}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] getShopItemPicture(@PathVariable("url") String url) throws IOException {
        String pictureUrl = shopItemPictureViewPath + shopItemPictureDefault;

        if (url!= null && !url.equals("null")) {
            pictureUrl = shopItemPictureViewPath + url + ".jpg";
        }

        InputStream inputStream;

        try {
            ClassPathResource resource = new ClassPathResource(pictureUrl);
            inputStream = resource.getInputStream();

        } catch (Exception e) {
            ClassPathResource resource = new ClassPathResource(shopItemPictureViewPath + shopItemPictureDefault);
            inputStream = resource.getInputStream();
        }

        return IOUtils.toByteArray(inputStream);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/shop_item_main_picture/{shop_item_id}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] getShopItemMainPicture(@PathVariable("shop_item_id") Long shopItemId) throws IOException{
        String pictureUrl = shopItemPictureViewPath + shopItemPictureDefault;
        List<Picture> pictures = shopItemService.getAllShopItemPictures(shopItemId);
        if (pictures != null && pictures.size() > 0) {
            pictureUrl = shopItemPictureViewPath + pictures.get(0).getUrl() + ".jpg";
        }

        InputStream inputStream;

        try {
            ClassPathResource resource = new ClassPathResource(pictureUrl);
            inputStream = resource.getInputStream();

        } catch (Exception e) {
            ClassPathResource resource = new ClassPathResource(shopItemPictureViewPath + shopItemPictureDefault);
            inputStream = resource.getInputStream();
        }

        return IOUtils.toByteArray(inputStream);
    }

    @PostMapping("/admin/add_shop_item_picture")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addShopItemPicture(@RequestParam("shop_item_picture") MultipartFile picture,
                                     @RequestParam("shop_item_id") Long shopItemId) {
        if (Objects.equals(picture.getContentType(), "image/jpeg") || Objects.equals(picture.getContentType(), "image/png")) {
            try {
                ShopItem shopItem = shopItemService.getShopItemById(shopItemId);

                if (shopItem != null) {
                    // Create empty picture just to get new unique id of the picture
                    Picture shopItemPicture = shopItemService.addPicture(new Picture());
                    // Now I know unique Id, I can use it to calculate sha1Hex
                    String pictureName = DigestUtils.sha1Hex("shop_item_picture" + shopItemPicture.getId() + "_!KEKULUSRIFT");
                    // Then I update values of the picture
                    shopItemPicture.setUrl(pictureName);
                    shopItemPicture.setAddedDate(new Date(new java.util.Date().getTime()));
                    shopItemPicture.setShopItem(shopItem);
                    // Then I update picture entry in the database
                    shopItemPicture = shopItemService.updatePicture(shopItemPicture);
                    // if Ok then I save the picture to the server
                    if (shopItemPicture != null) {
                        byte[] bytes = picture.getBytes();
                        Path path = Paths.get(shopItemPictureUploadPath + pictureName + ".jpg");
                        Files.write(path, bytes);
                    }

                    return "redirect:/admin/details/" + shopItemId + "?add_picture_success";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/admin/details/" + shopItemId + "?add_picture_error";
    }

    @PostMapping("/admin/remove_shop_item_picture")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String removeShopItemPicture(@RequestParam("picture_id") Long pictureId,
                                        @RequestParam("shop_item_id") Long shopItemId) {
        Picture picture = shopItemService.getPictureById(pictureId);
        if (picture != null) {
            Path path = Paths.get(shopItemPictureUploadPath + picture.getUrl() + ".jpg");
            //Files.delete(path);
            shopItemService.deletePicture(picture);
            return "redirect:/admin/details/" + shopItemId + "?remove_picture_success";
        }
        return "redirect:/admin/details/" + shopItemId + "?remove_picture_error";
    }

}
