package se.sakerhet.server.config;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final byte[] secretKey;

    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        this.secretKey = java.util.Base64.getDecoder().decode(secret); // Decode Base64 secret
    }

    // Generate JWT token
    public String generateToken(String email) {
        try {
            JWSSigner signer = new MACSigner(secretKey);

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(email)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet
            );

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Error generating JWT token: " + e.getMessage(), e);
        }
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secretKey);

            return signedJWT.verify(verifier) &&
                    new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime());
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    // Extract email from JWT token
    public String getEmailFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Error extracting email from token: " + e.getMessage(), e);
        }
    }



    // Generera en Base64-kodad nyckel (endast för engångsanvändning)

        public static void main(String[] args) throws NoSuchAlgorithmException {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(256); // Set key size
            SecretKey secretKey = keyGen.generateKey();
            String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            System.out.println("Base64 Encoded Key: " + base64Key);
        }
    }


/*
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
                .signWith(key, SignatureAlgorithm.HS256)  // Användning av SecretKey med algoritmen
                .compact();
    }

    // Hämta e-post från JWT-token
    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)  // Ensure this is the correct signing key
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            // Log the exception and return null if the token is invalid
            System.err.println("Invalid token: " + e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Log the reason for failure and return false
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }



    }*/

