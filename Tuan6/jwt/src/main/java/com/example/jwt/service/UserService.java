package com.example.jwt.service;

import com.example.jwt.authen.UserPrincipal;
import com.example.jwt.entity.User;

public interface UserService {
    User createUser(User user);
    UserPrincipal findByUsername(String username);
}
