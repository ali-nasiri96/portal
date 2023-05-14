package com.example.portal.service;


import com.example.portal.model.dto.internal.AccessToken;
import com.example.portal.model.dto.internal.UserType;
import com.example.portal.model.exception.UnauthorizedAccess;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private byte[] secret;
    private static final SecureRandom secureRandom = new SecureRandom();
    @Value("${access.token.secret}")
    private String _secret;


    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(_secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private byte[] getSecret() {
        if (this.secret == null) {
            this.secret = Base64.getDecoder().decode(_secret);
        }
        return this.secret;
    }

    public String generateToken(String userId, UserType userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_type", userType.name());
        return generateToken(userId, claims);
    }

    private String generateToken(String userId, Map<String, Object> claims) {
        Date createDate = new Date();
        String tokenId = String.valueOf(secureRandom.nextLong());
        JwtBuilder builder = Jwts.builder()
                .setId(tokenId)
                .setIssuedAt(createDate)
                .setSubject(userId)
                .addClaims(claims)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256);
        return builder.compact();
    }

    public AccessToken parseToken(String token) throws UnauthorizedAccess {
        AccessToken accessToken = new AccessToken();
        Jws<Claims> jws;
        try {
            jws = Jwts.parser()
                    .setSigningKey(getSignInKey())
                    .parseClaimsJws(token);

            accessToken.setIssuanceDate(jws.getBody().getIssuedAt());
            accessToken.setUserId(jws.getBody().getSubject());
            accessToken.setTokenId(jws.getBody().getId());
            String userType = (String) jws.getBody().get("user_type");
            if (userType != null && !userType.isEmpty())
                accessToken.setUserType(UserType.valueOf(userType));

            return accessToken;
        } catch (JwtException ex) {
            logger.error(ex.getMessage());
            throw new UnauthorizedAccess("500-Could not verify JWT token integrity!");
        }
    }
}
