package Birger.SMS.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Birger.SMS.dto.PossedeDTO;
import Birger.SMS.dto.PossedeResponseDTO;
import Birger.SMS.model.*;
import Birger.SMS.repository.*;

@Service
public class PossedeService {

    private final PossedeRepository possedeRepository;
    private final UserRepository userRepository;
    private final NumeroRepository numeroRepository;

    public PossedeService(PossedeRepository possedeRepository, UserRepository userRepository, NumeroRepository numeroRepository) {
        this.possedeRepository = possedeRepository;
        this.userRepository = userRepository;
        this.numeroRepository = numeroRepository;
    }

    // 🔹 Récupérer toutes les relations
    public List<Possede> getAllPossede() {
        return possedeRepository.findAll();
    }

    // 🔹 Créer une nouvelle relation
    @Transactional
    public PossedeResponseDTO createPossede(PossedeDTO dto) {
        // Vérifie si l'utilisateur existe
        User user = userRepository.findById(dto.getIdUtilisateur())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + dto.getIdUtilisateur()));

        // Vérifie si le numéro existe
        Numero numero = numeroRepository.findById(dto.getIdNumero())
                .orElseThrow(() -> new RuntimeException("Numéro introuvable avec l'ID : " + dto.getIdNumero()));

        // Vérifie si la relation existe déjà
        PossedeId id = new PossedeId(dto.getIdUtilisateur(), dto.getIdNumero());
        if (possedeRepository.existsById(id)) {
            throw new DataIntegrityViolationException("Cette relation utilisateur-numéro existe déjà !");
        }

        // Crée et sauvegarde la relation
        Possede possede = new Possede(user, numero);
        possedeRepository.save(possede);

        // Retourne un DTO pour la réponse JSON
        return new PossedeResponseDTO(user, numero);
    }

    // 🔹 Supprimer une relation
    @Transactional
    public void deletePossede(Long idUtilisateur, Long idNumero) {
        PossedeId id = new PossedeId(idUtilisateur, idNumero);

        Possede possede = possedeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucune relation trouvée pour l'utilisateur " 
                        + idUtilisateur + " et le numéro " + idNumero));

        possedeRepository.delete(possede);
    }
}
