package com.learn_spring_boot.service.impl;

import com.learn_spring_boot.dto.ApiResponseDto;
import com.learn_spring_boot.dto.SignInRequestDto;
import com.learn_spring_boot.dto.SignInResponseDto;
import com.learn_spring_boot.dto.SignUpRequestDto;
import com.learn_spring_boot.entity.Role;
import com.learn_spring_boot.entity.User;
import com.learn_spring_boot.exception.RoleNotFoundException;
import com.learn_spring_boot.exception.UserAlreadyExistsException;
import com.learn_spring_boot.factory.RoleFactory;
import com.learn_spring_boot.security.auth.JwtUtils;
import com.learn_spring_boot.security.auth.UserDetailsImpl;
import com.learn_spring_boot.service.AuthService;
import com.learn_spring_boot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final RoleFactory roleFactory;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;
    @Override
    @Transactional
    public ResponseEntity<ApiResponseDto<?>> signUp(SignUpRequestDto signUpRequestDto)
            throws UserAlreadyExistsException, RoleNotFoundException {
            if (userService.existByUsername(signUpRequestDto.username())){
                throw new UserAlreadyExistsException("Username already exist!");
            }
            if(userService.existByEmail(signUpRequestDto.email())){
                throw new UserAlreadyExistsException("Email already exist!");
            }
            User user=createUser(signUpRequestDto);
            userService.save(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            ApiResponseDto.builder()
                                    .status(String.valueOf(HttpStatus.CREATED))
                                    .message("User account has been successfully created!")
                                    .build()
                    );
    }
    @Override
    public ResponseEntity<ApiResponseDto<?>> signIn(SignInRequestDto signInRequestDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequestDto.email(), signInRequestDto.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        SignInResponseDto signInResponseDto = SignInResponseDto.builder()
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .id(userDetails.getId())
                .token(jwt)
                .type("Bearer")
                .roles(roles)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                ApiResponseDto.builder()
                        .status(String.valueOf(HttpStatus.OK))
                        .message("Sign in successfull!")
                        .response(signInResponseDto)
                        .build());
    }
    private User createUser(SignUpRequestDto signUpRequestDto) throws RoleNotFoundException {
        return User.builder()
                .email(signUpRequestDto.email())
                .username(signUpRequestDto.username())
                .password(passwordEncoder.encode(signUpRequestDto.password()))
                .enabled(true)
                .roles(determineRoles(signUpRequestDto.roles()))
                .build();
    }
    private Set<Role> determineRoles(Set<String> strRoles) throws RoleNotFoundException {
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            roles.add(roleFactory.getInstance("user"));
        } else {
            for (String role : strRoles) {
                roles.add(roleFactory.getInstance(role));
            }
        }
        return roles;
    }
    }
