package com.company.ecommerce.customer.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .filter(authentication -> authentication.getPrincipal() instanceof Jwt)
                .map(Authentication::getPrincipal)
                .map(Jwt.class::cast)
                .map(Jwt::getClaims)
                .map(claims -> claims.get("sub"))
                .map(Object::toString);
    }

}
