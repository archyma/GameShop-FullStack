package com.example.gameshopbackend.security;

import com.example.gameshopbackend.entity.Role;
import com.example.gameshopbackend.entity.User;
import com.example.gameshopbackend.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Role userRole = user.getRole();

        if (userRole == null) {
            throw new UsernameNotFoundException("User has no roles: " + username);
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(userRole.getName());
        Set<GrantedAuthority> grantedAuthorities = Collections.singleton(authority);

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
