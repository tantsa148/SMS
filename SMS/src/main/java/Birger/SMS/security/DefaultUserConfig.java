package Birger.SMS.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import Birger.SMS.model.User;
import Birger.SMS.repository.UserRepository;

@Configuration
public class DefaultUserConfig {

    @Bean
    CommandLineRunner createDefaultUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Vérifie si l'utilisateur existe déjà
            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin")); // mot de passe sécurisé
                user.setRole("ADMIN"); // selon ton modèle
                userRepository.save(user);
            }
        };
    }
}
