package com.mahmudalam.userauth.security.jwt;

import com.mahmudalam.userauth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Data
@Service
public class JwtService {
//    private final String secretKey;

    @Value("${jwt.access.secret}")
    private String accessSecret;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${jwt.access.expiry}")
    private Long accessTokenValidity;

    @Value("${jwt.refresh.expiry}")
    private Long refreshTokenValidity;

    /*
    public JwtService(){
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))  //10mnts
                .and()
                .signWith(getKey())
                .compact();
    }
    * */

    public String generateAccessToken(User user) {
        Map<String, Object> claims = Map.of("role", user.getRole().name());
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .and()
                .signWith(getAccessKey())
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(getRefreshKey())
                .compact();
    }

    public String extractUsernameFromRefresh(String token) {
        return extractClaim(token, Claims::getSubject, getRefreshKey());
    }

    public boolean isRefreshTokenValid(String token) {
        return !isTokenExpired(token, getRefreshKey());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver, SecretKey key) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return resolver.apply(claims);
    }

    private boolean isTokenExpired(String token, SecretKey key) {
        Date expiry = extractClaim(token, Claims::getExpiration, key);
        return expiry.before(new Date());
    }

    private SecretKey getAccessKey() {
        byte[] keyBytes = Decoders.BASE64.decode(accessSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getRefreshKey() {
        byte[] keyBytes = Decoders.BASE64.decode(refreshSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // extract username from access token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject, getAccessKey());
    }

    // validate access token validity and ownership
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, getAccessKey()));
    }

    /*
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> clamResolver) {
        final Claims claims = extractAllClaims(token);
        return clamResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException ex) {
            throw new RuntimeException("Invalid JWT token", ex);
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    * */
}
