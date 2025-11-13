package Birger.SMS.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Birger.SMS.model.Plateforme;

@Repository
public interface PlateformeRepository extends JpaRepository<Plateforme, Long> {
    // JpaRepository fournit déjà toutes les opérations CRUD
}
