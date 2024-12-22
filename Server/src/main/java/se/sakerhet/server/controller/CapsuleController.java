package se.sakerhet.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.sakerhet.server.entity.Capsule;
import se.sakerhet.server.entity.User;
import se.sakerhet.server.service.CapsuleService;
import se.sakerhet.server.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/capsules")
public class CapsuleController {

    private final CapsuleService capsuleService;
    private final UserService userService;

    public CapsuleController(CapsuleService capsuleService, UserService userService) {
        this.capsuleService = capsuleService;
        this.userService = userService;
    }

    // Endpoint to create a new capsule
    @PostMapping("/create")
    public ResponseEntity<String> createCapsule(@RequestBody String message, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User user = userService.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Capsule capsule = capsuleService.createCapsule(user, message);
            return ResponseEntity.ok("Capsule created with ID: " + capsule.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating capsule: " + e.getMessage());
        }
    }

    // Endpoint to retrieve all capsules for the authenticated user
    @GetMapping
    public ResponseEntity<List<Capsule>> getCapsules(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User user = userService.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Capsule> capsules = capsuleService.getCapsulesByUser(user);
            return ResponseEntity.ok(capsules);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}


/*

@RestController
@RequestMapping("/capsules")
public class CapsuleController {

    private final CapsuleService capsuleService;
    private final UserService userService;

    public CapsuleController(CapsuleService capsuleService, UserService userService) {
        this.capsuleService = capsuleService;
        this.userService = userService;
    }

    // Create a new capsule
    @PostMapping("/create")
    public ResponseEntity<String> createCapsule(@RequestBody String message, Authentication authentication) throws Exception {
        String userEmail = authentication.getName();
        User user = userService.findByEmail(userEmail).orElseThrow(() ->
                new RuntimeException("User not found"));

        Capsule capsule = capsuleService.createCapsule(user, message);
        return ResponseEntity.ok("Capsule created with ID: " + capsule.getId());
    }

    // Retrieve all capsules for the authenticated user
    @GetMapping
    public ResponseEntity<List<Capsule>> getCapsules(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User user = userService.findByEmail(userEmail).orElseThrow(() ->
                    new RuntimeException("User not found"));

            List<Capsule> capsules = capsuleService.getCapsulesByUser(user);
            return ResponseEntity.ok(capsules);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
*/





/*
package se.sakerhet.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import se.sakerhet.server.config.JwtTokenProvider;
import se.sakerhet.server.entity.User;
import se.sakerhet.server.entity.Capsule;
import se.sakerhet.server.service.CapsuleService;
import se.sakerhet.server.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@RequestMapping("/api/capsules")
public class CapsuleController {

    private final CapsuleService capsuleService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public CapsuleController(CapsuleService capsuleService, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.capsuleService = capsuleService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }


    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(HttpServletRequest request, @RequestParam String recipientEmail, @RequestParam String message) throws Exception {
        // Hämta användarens token
        String token = request.getHeader("Authorization").substring(7);
        String senderEmail = jwtTokenProvider.getEmailFromToken(token);

        // Hämta mottagaren från databasen
        Optional<User> recipient = userService.findByEmail(recipientEmail);
        if (recipient.isPresent()) {
            // Kryptera meddelandet
            String encryptedMessage = CapsuleService.encryptMessage(message);
            // Skicka det krypterade meddelandet
            // Logik för att skicka meddelandet kan läggas till här, t.ex. spara i databas eller skicka via HTTP.

            return ResponseEntity.ok("Message sent: " + encryptedMessage);
        }
        return ResponseEntity.badRequest().body("Recipient not found");
    }

    @PostMapping("/receive")
    public ResponseEntity<String> receiveMessage(HttpServletRequest request, @RequestParam String encryptedMessage) throws Exception {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtTokenProvider.getEmailFromToken(token);

        // Dekryptera meddelandet
        String decryptedMessage = CapsuleService.decryptMessage(encryptedMessage);

        return ResponseEntity.ok("Decrypted message: " + decryptedMessage);
    }
}*/
