package com.learn_spring_boot.service;

import com.learn_spring_boot.entity.User;

public interface UserService {
    boolean existByUsername(String username);
    boolean existByEmail(String email);
    void save(User user);
}
