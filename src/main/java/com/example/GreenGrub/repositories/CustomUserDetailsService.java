package com.example.GreenGrub.repositories;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.GreenGrub.dto.CustomUserDetails;
import com.example.GreenGrub.entity.User;
import com.example.GreenGrub.exception.ExceptionFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;
    private final ExceptionFactory exceptionFactory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(
            () -> exceptionFactory.userNotFoundException(username)
        );
        return new CustomUserDetails(user);
    }
    
}
