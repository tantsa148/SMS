package Birger.SMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Birger.SMS.model.Possede;
import Birger.SMS.model.PossedeId;

public interface PossedeRepository extends JpaRepository<Possede, PossedeId> {

    // ⚡ méthode pour récupérer toutes les possessions d'un utilisateur
    List<Possede> findByUtilisateur_Id(Long userId);
}
