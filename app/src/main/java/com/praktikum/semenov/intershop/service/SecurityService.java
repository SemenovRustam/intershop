package com.praktikum.semenov.intershop.service;

import com.praktikum.semenov.intershop.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SecurityService {

    public Mono<Long> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication())
                .filter(Authentication::isAuthenticated)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .cast(Authentication.class)
                .map(authentication -> (CustomUserDetails) authentication.getPrincipal())
                .map(CustomUserDetails::getId);
    }
}
