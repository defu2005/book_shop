package com.learn_spring_boot.security.auth;

import com.learn_spring_boot.entity.User;
import com.learn_spring_boot.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email)throws UsernameNotFoundException {
        User user=userRepository
                .findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found with email: "+email));
        return UserDetailsImpl.build(user);
    }
}
