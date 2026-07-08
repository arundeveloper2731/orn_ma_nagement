package com.example.orn.service;

import com.example.orn.model.User;

public interface UserService {
    User registerUser(User user);
     User saveUser(User user);

    User findByUserName(String username);
    
    boolean existsByUsername(String username);
}
