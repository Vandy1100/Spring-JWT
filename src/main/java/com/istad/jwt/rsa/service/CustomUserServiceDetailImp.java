package com.istad.jwt.rsa.service;

import com.istad.jwt.rsa.model.User;
import com.istad.jwt.rsa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserServiceDetailImp implements UserDetailsService {
    //load  user from the database
    //inject repository
    @Autowired
    final UserRepository userRepository;

    public CustomUserServiceDetailImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user=userRepository.findUserByName(name).stream().findAny().orElse(null);
        System.out.println("here is the authentication : " +user);
        if(user==null){
            throw new UsernameNotFoundException("Authentication User don't exist!");
        }
        org.springframework.security.core.userdetails.User user1= (org.springframework.security.core.userdetails.User) org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole()).build();
        return  user1;
    }
}
