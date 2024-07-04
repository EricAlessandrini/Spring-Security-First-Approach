package com.security.practice.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.security.practice.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class JwtTokenValidationFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;

    public JwtTokenValidationFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(jwtToken != null) {
            jwtToken = jwtToken.substring(7);
            DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

            String username = jwtUtils.extractUsernameFromToken(decodedJWT);
            String stringAuthorities = jwtUtils.extractSpecificClaim(decodedJWT, "authorities").asString();

            Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(new UsernamePasswordAuthenticationToken(username, null, authorities));
            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }
}
