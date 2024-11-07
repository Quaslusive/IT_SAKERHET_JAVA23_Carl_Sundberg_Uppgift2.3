package se.sakerhet.server.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final SecretKey key;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    // JWT-konfiguration hämtad från application.properties
    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        logger.info("Initializing JwtTokenProvider with secret: {}", secret);  // Loggning för att visa den laddade nyckeln
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));  // Hanterar nyckeln med Base64-dekodning
    }

    // Generera JWT-token
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)  // Användning av SecretKey med algoritmen
                .compact();
    }

    // Hämta e-post från JWT-token
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)  // Använd nyckeln för att verifiera token
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // Validera JWT-token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)  // Använd nyckeln för verifiering
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Hantera undantag om token är ogiltig
            return false;
        }
    }

    // Generera en Base64-kodad nyckel (endast för engångsanvändning)
    public static class KeyGenerator {
        public static void main(String[] args) {
            byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
            String base64Key = Base64.getEncoder().encodeToString(keyBytes);
            System.out.println("Generated Base64 Key: " + base64Key);
        }
    }
}
