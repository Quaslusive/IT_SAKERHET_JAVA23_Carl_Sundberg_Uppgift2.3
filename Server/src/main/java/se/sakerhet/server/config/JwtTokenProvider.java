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
        this.secretKey = java.util.Base64.getDecoder().decode(secret);
    }

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

    public String getEmailFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Error extracting email from token: " + e.getMessage(), e);
        }
    }
}

/*

    // Generera en Base64-kodad nyckel (endast för engångsanvändning)
        public static void main(String[] args) throws NoSuchAlgorithmException {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(256); // Set key size
            SecretKey secretKey = keyGen.generateKey();
            String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            System.out.println("Base64 Encoded Key: " + base64Key);
        }
    }
*/
