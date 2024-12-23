package dev.sodra6.ygcho.jwt.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@SuppressWarnings("deprecation")
@RequiredArgsConstructor
public class JwtUtil {

    @Value("1800")
    private long ACCESS_TOKEN_EXPIRE;

    @Value("7200")
    private long REFRESH_TOKEN_EXPIRE;

    private final SecretKeyUtil keyUtil;

    /**
     * jwt 생성
     * @param usrId
     * @param usrNm
     * @param usrAuth
     * @param subject
     * @param expireMin
     * @return
     */
    public String create(String usrId, String usrNm, String usrAuth, String subject, long expireMin){
        Claims claims = Jwts.claims();

        claims.put("usrId", usrId);
        claims.put("usrNm", usrNm);
        claims.put("usrAuth", usrAuth);

        String jwt = Jwts.builder()
                .setClaims(claims)// 담고 싶은 정보 설정
                .setSubject(subject)// 토큰 제목 설정(accessToken or refreshToken 인지 구별)
                .setExpiration(new Date(System.currentTimeMillis() + expireMin))// 유효기간
                .signWith(SignatureAlgorithm.HS256, keyUtil.getSecretKey())// signature -secret key 이용한 암호화
                .compact();//마지막 직렬화

        //log.info("create token : {}", jwt);
        return jwt;
    }

    /**
     * Access 토큰 생성
     * @param usrId
     * @param usrNm
     * @param usrAuth
     * @return
     */
    public String createAccessToken(String usrId, String usrNm, String usrAuth){
        return create(usrId, usrNm, usrAuth,"accessToken", ACCESS_TOKEN_EXPIRE * 1000L);
    }

    /**
     * Refresh 토큰 생성
     * @return
     */
    public String createRefreshToken(){
        return create(null, null, null, "refreshToken", REFRESH_TOKEN_EXPIRE * 1000L);
    }

    public Claims validateTokenAndReturn(String token){
        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(keyUtil.getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims;

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

    }

}
