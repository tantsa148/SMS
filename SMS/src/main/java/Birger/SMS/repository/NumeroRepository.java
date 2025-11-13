package Birger.SMS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Birger.SMS.model.Numero;

@Repository
public interface NumeroRepository extends JpaRepository<Numero, Long> {
    Optional<Numero> findByValeurNumero(String valeurNumero);
    boolean existsByValeurNumero(String valeurNumero);
}
