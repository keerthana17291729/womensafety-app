package com.example.womensafety.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    // create token (expires in 7 days)
    public String generateToken(Long userId, String email) {
        Algorithm alg = Algorithm.HMAC256(jwtSecret);
        return JWT.create()
                .withClaim("userId", userId)
                .withClaim("email", email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 7L*24*3600*1000))
                .sign(alg);
    }

    public DecodedJWT verifyToken(String token) {
        Algorithm alg = Algorithm.HMAC256(jwtSecret);
        return JWT.require(alg).build().verify(token);
    }
}
