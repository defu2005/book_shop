package com.learn_spring_boot.service.impl;

import com.learn_spring_boot.entity.User;
import com.learn_spring_boot.repository.UserRepository;
import com.learn_spring_boot.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    @Override
    public boolean existByUsername(String username){
        return userRepository.existsByUsername(username);
    }
    @Override
    public boolean existByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    @Override
    public void save(User user){
        saveUser(user);
    }

    @Transactional
    public void saveUser(User user){
        userRepository.save(user);
    }
}
