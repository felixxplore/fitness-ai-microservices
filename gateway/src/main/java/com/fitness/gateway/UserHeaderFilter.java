package com.fitness.gateway;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class UserHeaderFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return exchange.getPrincipal()
                .cast(Authentication.class)
                .flatMap(auth -> {

                    Jwt jwt = (Jwt) auth.getPrincipal();
                    String userId = jwt.getSubject();

                    ServerHttpRequest request = exchange.getRequest()
                            .mutate()
                            .header("X-User-Id", userId)
                            .build();

                    return chain.filter(exchange.mutate().request(request).build());
                });
    }
}
