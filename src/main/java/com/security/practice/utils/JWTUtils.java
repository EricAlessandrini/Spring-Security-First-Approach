package com.security.practice.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JWTUtils {

    @Value("${security.jwt.user.generator}")
    private String tokenIssuer;

    @Value("${security.jwt.key.private}")
    private String privateKey;

    public String createJwtToken(Authentication authentication) {

        /*Estuve investigando sobre este algoritmo con el que se encripta y lo han diseñado para que solamente utilize una clave privada para encriptar y desencriptar.
        * Si por ejemplo usaras una que es RSAXXX ahi si te pide las dos claves... Supongo que usa este algoritmo mas por una cuestion de aprendizaje y no de buenas practicas
        * de seguridad...*/
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

        String username = authentication.getPrincipal().toString();

        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return JWT.create()
                .withIssuer(this.tokenIssuer)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 20000000))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

            /*Esta dependencia de JWT tiene todos sus propios metodos que hacen toda la movida de encriptacion y desencriptacion por su cuenta por eso me parecia raro
            * que no anduviera o que me tirara la excepcion... */
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.tokenIssuer)
                    .build();

            decodedJWT = verifier.verify(token);
            return decodedJWT;

        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Invalid token");
        }
    }

    public String extractUsernameFromToken (DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public Claim extractSpecificClaim (DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    public Map<String, Claim> extractAllClaims(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }
}
