package se.sakerhet.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.sakerhet.server.dto.CapsuleRequest;
import se.sakerhet.server.entity.Capsule;
import se.sakerhet.server.entity.User;
import se.sakerhet.server.service.CapsuleService;
import se.sakerhet.server.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/auth/capsules")
public class CapsuleController {

    private static final Logger logger = LoggerFactory.getLogger(CapsuleController.class);

    private final CapsuleService capsuleService;
    private final UserService userService;

    public CapsuleController(CapsuleService capsuleService, UserService userService) {
        this.capsuleService = capsuleService;
        this.userService = userService;
    }
    @PostMapping("/create")
    public ResponseEntity<String> createCapsule(@RequestBody CapsuleRequest capsuleRequest, Authentication authentication) {
        try {
            User user = userService.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Capsule capsule = capsuleService.createCapsule(user, capsuleRequest.getMessage());
            return ResponseEntity.ok("Capsule created with ID: " + capsule.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating capsule: " + e.getMessage());
        }
    }


/*
    @PostMapping("/create")
    public ResponseEntity<String> createCapsule(@RequestBody CapsuleRequest request, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            logger.info("Creating capsule for user: {}", userEmail);

            User user = userService.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Capsule capsule = capsuleService.createCapsule(user, request.getMessage());
            logger.info("Capsule created with ID: {}", capsule.getId());

            return ResponseEntity.ok("Capsule created with ID: " + capsule.getId());
        } catch (Exception e) {
            logger.error("Error creating capsule: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error creating capsule: " + e.getMessage());
        }
    }*/

    @GetMapping
    public ResponseEntity<List<Capsule>> getCapsules(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            logger.info("Retrieving capsules for user: {}", userEmail);

            User user = userService.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Capsule> capsules = capsuleService.getCapsulesByUser(user);
            logger.info("Capsules retrieved for user: {}", userEmail);

            return ResponseEntity.ok(capsules);
        } catch (Exception e) {
            logger.error("Error retrieving capsules: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/decrypted")
    public ResponseEntity<List<String>> getDecryptedCapsules(Authentication authentication) {
        try {
            String email = authentication.getName();
            logger.info("Retrieving decrypted capsules for user: {}", email);

            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<String> decryptedCapsules = capsuleService.getDecryptedCapsules(user);
            return ResponseEntity.ok(decryptedCapsules);
        } catch (Exception e) {
            logger.error("Error retrieving decrypted capsules: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}
