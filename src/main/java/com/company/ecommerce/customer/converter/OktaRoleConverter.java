package com.company.ecommerce.customer.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OktaRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        if (Objects.isNull(jwt.getClaims())) {
            return List.of();
        }

        final List<String> roles = (List<String>) jwt.getClaims().get("roles");
        return roles.stream()
                .map(String::toUpperCase)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
