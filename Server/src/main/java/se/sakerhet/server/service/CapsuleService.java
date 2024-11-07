package se.sakerhet.server.service;


import org.springframework.stereotype.Service;
import se.sakerhet.server.repository.CapsuleRepository;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class CapsuleService {

    private final CapsuleRepository capsuleRepository;
    private static final String secretKey = "aesEncryptionKey";  // Minst 16 tecken

    public CapsuleService(CapsuleRepository capsuleRepository) {
        this.capsuleRepository = capsuleRepository;
    }
/*
    // Metod för att kryptera och skapa en ny tidskapsel
    public Capsule createCapsule(User user, String message) throws Exception {
        Capsule capsule = new Capsule();
        capsule.setUser(user);  // Länka kapseln till användaren

        // Kryptera meddelandet och sätt det krypterade meddelandet i kapseln
        byte[] encryptedMessage = encrypt(message);
        capsule.setEncryptedMessage(encryptedMessage);

        // Spara kapseln i databasen
        return capsuleRepository.save(capsule);
    }*/

    public static String encryptMessage(String message) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedMessage = cipher.doFinal(message.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedMessage);
    }

    public static String decryptMessage(String encryptedMessage) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodedMessage = Base64.getDecoder().decode(encryptedMessage);
        return new String(cipher.doFinal(decodedMessage), "UTF-8");
    }
}