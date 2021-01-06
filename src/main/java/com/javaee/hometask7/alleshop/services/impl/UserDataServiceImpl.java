package com.javaee.hometask7.alleshop.services.impl;

import com.javaee.hometask7.alleshop.models.User;
import com.javaee.hometask7.alleshop.services.UserDataService;
import com.javaee.hometask7.alleshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserDataServiceImpl implements UserDataService {

    private final UserService userService;

    @Autowired
    public UserDataServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            return userService.getUserByEmail(securityUser.getUsername());
        }
        return null;
    }
}
