package com.base.todo.todosecurityhibernate.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryUserServiceImpl implements UserDetailsService {

    private final Map<String, UserDetails> userMap = new ConcurrentHashMap<>();
    String adminHash = new BCryptPasswordEncoder().encode("p");
    String userHash = new BCryptPasswordEncoder().encode("p");
    public InMemoryUserServiceImpl() {
        // In production, load from DB. Here we hardcode for demo.
        userMap.put("admin", User.builder()
                .username("admin")
                .password(adminHash) // "password"
                .roles("ADMIN")
                .build());

        userMap.put("user", User.builder()
                .username("user")
                .password(userHash) // "password"
                .roles("USER")
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = SerializationUtils.clone(userMap.get(username));
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        System.out.println("=== Stored: " + user.getPassword());
        System.out.println("=== Match: " + new BCryptPasswordEncoder().matches("password", user.getPassword()));
        return user;
    }
}   