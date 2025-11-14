package Birger.SMS.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import Birger.SMS.dto.PossedeDTO;
import Birger.SMS.dto.PossedeResponseDTO;
import Birger.SMS.model.Numero;
import Birger.SMS.model.Possede;
import Birger.SMS.model.PossedeId;
import Birger.SMS.model.User;
import Birger.SMS.repository.NumeroRepository;
import Birger.SMS.repository.PossedeRepository;
import Birger.SMS.repository.UserRepository;

@Service
public class PossedeService {

    private static final Logger logger = LoggerFactory.getLogger(PossedeService.class);

    private final PossedeRepository possedeRepository;
    private final UserRepository userRepository;
    private final NumeroRepository numeroRepository;

    public PossedeService(PossedeRepository possedeRepository, 
                          UserRepository userRepository, 
                          NumeroRepository numeroRepository) {
        this.possedeRepository = possedeRepository;
        this.userRepository = userRepository;
        this.numeroRepository = numeroRepository;
    }

    // ---------------- GET ----------------
    public List<PossedeResponseDTO> getPossedeByUserId(Long userId) {
        logger.info("Récupération des association pour l'utilisateur id={}", userId);
        return possedeRepository.findByUtilisateur_Id(userId).stream()
                .map(p -> new PossedeResponseDTO(p.getUtilisateur(), p.getNumero()))
                .peek(pdto -> logger.info("Association : utilisateur={} / numero={}", pdto.getUsername(), pdto.getValeurNumero()))
                .collect(Collectors.toList());
    }

    // ---------------- CREATE ----------------
    public PossedeResponseDTO createPossede(PossedeDTO dto) {
        logger.info("Création Associtation : utilisateurId={}, numeroId={}", dto.getIdUtilisateur(), dto.getIdNumero());

        User user = userRepository.findById(dto.getIdUtilisateur())
                .orElseThrow(() -> {
                    logger.error("Utilisateur introuvable id={}", dto.getIdUtilisateur());
                    return new RuntimeException("Utilisateur introuvable");
                });

        Numero numero = numeroRepository.findById(dto.getIdNumero())
                .orElseThrow(() -> {
                    logger.error("Numéro introuvable id={}", dto.getIdNumero());
                    return new RuntimeException("Numéro introuvable");
                });

        PossedeId possedeId = new PossedeId(user.getId(), numero.getId());
        if (possedeRepository.existsById(possedeId)) {
            logger.warn("Cette association existe déjà : utilisateurId={} / numeroId={}", user.getId(), numero.getId());
            throw new RuntimeException("Cette association existe déjà !");
        }

        Possede possede = new Possede(user, numero);
        Possede saved = possedeRepository.save(possede);
        logger.info("Association créée avec succès : utilisateur={} / numero={}", user.getUsername(), numero.getValeurNumero());

        return new PossedeResponseDTO(saved.getUtilisateur(), saved.getNumero());
    }

    // ---------------- DELETE ----------------
    public void deletePossede(Long userId, Long numeroId) {
        logger.info("Suppression d'une association : utilisateurId={}, numeroId={}", userId, numeroId);

        PossedeId id = new PossedeId(userId, numeroId);
        if (!possedeRepository.existsById(id)) {
            logger.error("Association non trouvée pour suppression : utilisateurId={}, numeroId={}", userId, numeroId);
            throw new RuntimeException("Association non trouvée pour suppression !");
        }

        possedeRepository.deleteById(id);
        logger.info("Association supprimée avec succès : utilisateurId={}, numeroId={}", userId, numeroId);
    }
}
