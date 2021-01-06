package com.javaee.hometask7.alleshop.services;

import com.javaee.hometask7.alleshop.models.Role;
import com.javaee.hometask7.alleshop.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;

    User getUserByEmail(String email);

    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    List<Role> getAllRoles();

    boolean saveUser(User user);

    boolean deleteUser(User user);

    Role getRoleById(Long id);
}
