package com.praktikum.semenov.intershop.security;

import com.praktikum.semenov.intershop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    @Override
        public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User with name " + username + " not found!")))
                .map(user -> {
                    log.info("find user {}", user.getUsername());
                    List<GrantedAuthority> authorities  = Arrays.stream(user.getRoles().split(","))
                            .map(String::trim)
                            .map(SimpleGrantedAuthority::new)
                            .map(auth -> (GrantedAuthority) auth)
                            .toList();

                    return new CustomUserDetails(
                            user.getId(),
                            user.getUsername(),
                            user.getPassword(),
                            authorities
                    );
                });
    }
}
