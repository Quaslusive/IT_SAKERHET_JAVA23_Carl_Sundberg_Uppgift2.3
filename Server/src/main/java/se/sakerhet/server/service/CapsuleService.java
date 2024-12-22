package se.sakerhet.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.sakerhet.server.entity.Capsule;
import se.sakerhet.server.entity.User;
import se.sakerhet.server.repository.CapsuleRepository;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CapsuleService {

    private final CapsuleRepository capsuleRepository;

    @Value("${encryption.secret}")
    private String secretKey;

    public CapsuleService(CapsuleRepository capsuleRepository) {
        this.capsuleRepository = capsuleRepository;
    }

    // Create a new capsule
    public Capsule createCapsule(User user, String message) {
        try {
            Capsule capsule = new Capsule();
            capsule.setUser(user);
            byte[] encryptedMessage = encryptMessage(message);
            capsule.setEncryptedMessage(encryptedMessage);
            return capsuleRepository.save(capsule);
        } catch (Exception e) {
            throw new RuntimeException("Error creating capsule: " + e.getMessage(), e);
        }
    }

    // Retrieve capsules for a specific user
    public List<Capsule> getCapsulesByUser(User user) {
        return capsuleRepository.findByUser(user);
    }

    // Retrieve and decrypt capsules for a specific user
    public List<String> getDecryptedCapsules(User user) {
        try {
            List<Capsule> capsules = capsuleRepository.findByUser(user);
            List<String> decryptedMessages = new ArrayList<>();
            for (Capsule capsule : capsules) {
                decryptedMessages.add(decryptMessage(capsule.getEncryptedMessage()));
            }
            return decryptedMessages;
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting capsules: " + e.getMessage(), e);
        }
    }

    // Encrypt message
    public byte[] encryptMessage(String message) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(message.getBytes("UTF-8"));
    }

    // Decrypt message
    public String decryptMessage(byte[] encryptedMessage) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return new String(cipher.doFinal(encryptedMessage), "UTF-8");
    }
}
