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

import Birger.SMS.model.User;
import Birger.SMS.repository.UserRepository;
import Birger.SMS.security.JwtUtil;

@RestController
@RequestMapping("/")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> userMap) {
        String username = userMap.get("username");
        String password = userMap.get("password");

        // 🔍 Vérification des champs
        if (username == null || username.trim().isEmpty()) {
            logger.error("❌ Nom d'utilisateur vide");
            return ResponseEntity.badRequest().body("Le nom d'utilisateur ne peut pas être vide");
        }

        if (password == null || password.trim().isEmpty()) {
            logger.error("❌ Mot de passe vide pour l'utilisateur : {}", username);
            return ResponseEntity.badRequest().body("Le mot de passe ne peut pas être vide");
        }

        try {
            // Authentification via Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Récupération de l'utilisateur pour son ID
            User userEntity = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable après authentification"));

            // Génération du token avec ID et username
            String token = JwtUtil.generateToken(userEntity.getId(), username);

            logger.info("✅ Connexion réussie pour l'utilisateur '{}' (ID = {})", username, userEntity.getId());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "userId", userEntity.getId(),
                    "username", username
            ));

        } catch (AuthenticationException e) {
            logger.error("❌ Échec d'authentification pour '{}': {}", username, e.getMessage());
            return ResponseEntity.status(401).body("Nom d'utilisateur ou mot de passe invalide");
        }
    }
}
