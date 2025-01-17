package fr.upem.devops.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import fr.upem.devops.model.Staff;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JWTService {
    private Algorithm algorithm;
    private int defaultExpiration;

    public JWTService(
            @Value("jwt.secret") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.defaultExpiration = (60 * 60 * 24); // 1 day in seconds

    }

    public String create(String username, Staff profile) {
        Instant issuedAt = Instant.now();
        return JWT.create()
                .withIssuedAt(Date.from(issuedAt))
                .withExpiresAt(Date.from(issuedAt.plusSeconds(defaultExpiration)))
                .withClaim("username", username)
                .withClaim("id", profile.getId().toString())
                .withClaim("role", profile.getRole().name())
                .sign(algorithm);
    }

    public Map<String, Object> verify(String token) throws TokenVerificationException {
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaims().entrySet()
                    .stream()
                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().as(Object.class)));
        } catch (Exception e) {
            throw new TokenVerificationException(e);
        }
    }
}
