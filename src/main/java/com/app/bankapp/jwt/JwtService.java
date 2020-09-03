package com.app.bankapp.jwt;



import com.app.bankapp.model.User;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

@Component
public class JwtService {

    static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);
    static Gson gson = new Gson();

    public String createToken(String userName, Map<String, Object> claims, String secret, Date expireAt) {
        if (StringUtils.hasText(secret) && expireAt != null && expireAt.after(new Date())) {
            String secret2 = new String(Base64.encodeBase64(secret.getBytes()));
            String compactJws = Jwts.builder()
                    .setSubject(userName)
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.HS512, secret2)
                    .setExpiration(expireAt)
                    .compact();
            return compactJws;
        }
        return null;
    }

    public User isValid(String token, String secret) {
        if (StringUtils.hasText(token) && StringUtils.hasText(secret)) {
            try {
                String secret2 = new String(Base64.encodeBase64(secret.getBytes()));
                Claims claims = Jwts.parser().setSigningKey(secret2).parseClaimsJws(token).getBody();
                String object = gson.toJson(claims.get("user"));
                User user = gson.fromJson(object, User.class);
                return user;
            } catch (JwtException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return null;
    }




}
