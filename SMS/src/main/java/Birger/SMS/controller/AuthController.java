package Birger.SMS.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.security.JwtUtil;

@RestController
@RequestMapping("/")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");

        // 🔍 Vérification si les champs sont vides
        if (username == null || username.trim().isEmpty()) {
            logger.error("❌ nom d'utilisateur vide");
            return ResponseEntity.badRequest().body("Le nom d'utilisateur ne peut pas être vide");
        }

        if (password == null || password.trim().isEmpty()) {
            logger.error("❌ mot de passe vide (utilisateur : {})", username);
            return ResponseEntity.badRequest().body("Le mot de passe ne peut pas être vide");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            String token = JwtUtil.generateToken(username);
            logger.info("✅ Connexion réussie pour l'utilisateur '{}'", username);
            return ResponseEntity.ok(Map.of("token", token));

        } catch (AuthenticationException e) {
            logger.error("❌ Échec d'authentification pour '{}': {}", username, e.getMessage());
            return ResponseEntity.status(401).body("Nom d'utilisateur ou mot de passe invalide");
        }
    }
}


