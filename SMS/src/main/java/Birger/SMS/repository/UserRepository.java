package Birger.SMS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Birger.SMS.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Cherche un utilisateur par son nom d'utilisateur
    Optional<User> findByUsername(String username);

    // Vérifie si un utilisateur existe déjà
    boolean existsByUsername(String username);
}
