package dev.sodra6.ygcho.jwt.util;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class SecretKeyUtil {

    @Value("yg-ygcho-jwt-authorization-token-secret-key")
    private String SECRET_KEY;

    private String encodeBase64(){return Encoders.BASE64.encode(SECRET_KEY.getBytes(StandardCharsets.UTF_8));};

    public Key getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(encodeBase64());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
