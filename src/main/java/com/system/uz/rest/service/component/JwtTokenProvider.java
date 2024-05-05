package com.system.uz.rest.service.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.system.uz.exceptions.BadRequestException;
import com.system.uz.exceptions.UnauthorizedException;
import com.system.uz.global.JwtConstant;
import com.system.uz.global.MessageKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String generateJwtToken(String userId) {
        return JWT.create()
                .withIssuer(JwtConstant.TOKEN_ISSUER)
                .withSubject(userId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtConstant.ACCESS_EXPIRY))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }


    public String getSubjectFromToken(String token) {
        JWTVerifier verifier = getJwtVerifier();
        return verifier.verify(token).getSubject();
    }


    public boolean isValidToken(String username, String token) {
        JWTVerifier verifier = getJwtVerifier();
        return Objects.nonNull(username) && !isTokenExpired(verifier, token);
    }


    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expirationDate = verifier.verify(token).getExpiresAt();
        return expirationDate.before(new Date());
    }


    private JWTVerifier getJwtVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(JwtConstant.TOKEN_ISSUER).build();
        } catch (JWTVerificationException verificationException) {
            throw new JWTVerificationException(JwtConstant.TOKEN_CANNOT_BE_VERIFIED);
        }
        return verifier;
    }

}
