package com.javaee.hometask7.alleshop.services.impl;

import com.javaee.hometask7.alleshop.models.Role;
import com.javaee.hometask7.alleshop.models.User;
import com.javaee.hometask7.alleshop.repositories.RoleRepository;
import com.javaee.hometask7.alleshop.repositories.UserRepository;
import com.javaee.hometask7.alleshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

//    @Autowired
//    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.roleRepository = roleRepository;
//    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(s);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getRoles());
        }

        throw new UsernameNotFoundException("Username not found");
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createUser(User user) {
        User checkUser = userRepository.findByEmail(user.getEmail());
        if (checkUser == null) {
            Role role = roleRepository.findByName("ROLE_USER");
            if (role != null) {
                List<Role> roles = new ArrayList<>();
                roles.add(role);
                user.setRoles(roles);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                return userRepository.save(user);
            }
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public boolean saveUser(User user) {
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean deleteUser(User user) {
        userRepository.delete(user);
        return true;
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.getOne(id);
    }
}
