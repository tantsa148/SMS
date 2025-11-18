package Birger.SMS.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Birger.SMS.dto.DisponibleSurDTO;
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
public class NumeroAssignService {

    private static final Logger logger = LoggerFactory.getLogger(NumeroAssignService.class);

    private final NumeroService numeroService;
    private final UserRepository userRepository;
    private final NumeroRepository numeroRepository;
    private final PossedeRepository possedeRepository;
    private final DisponibleSurService disponibleSurService;

    public NumeroAssignService(
            NumeroService numeroService,
            UserRepository userRepository,
            NumeroRepository numeroRepository,
            PossedeRepository possedeRepository,
            DisponibleSurService disponibleSurService
    ) {
        this.numeroService = numeroService;
        this.userRepository = userRepository;
        this.numeroRepository = numeroRepository;
        this.possedeRepository = possedeRepository;
        this.disponibleSurService = disponibleSurService;
    }

    @Transactional
    public PossedeResponseDTO createNumeroAndPossede(PossedeDTO dto) {

        List<String> messages = new ArrayList<>(); // ← pour stocker les erreurs ou avertissements

        // 🔹 Récupérer l'utilisateur via JWT
        logger.info("Création numéro + association pour utilisateurId={}", dto.getIdUtilisateur());

        User user = userRepository.findById(dto.getIdUtilisateur())
                .orElseThrow(() -> {
                    logger.error("Utilisateur introuvable id={}", dto.getIdUtilisateur());
                    return new RuntimeException("Utilisateur introuvable");
                });

        // 🔹 Vérifier si le numéro existe déjà
        Numero numero = numeroRepository.findByValeurNumero(dto.getValeurNumero()).orElse(null);

        if (numero != null) {
            logger.info("Numéro {} existe déjà. Utilisation du numéro existant.", dto.getValeurNumero());
        } else {
            numero = new Numero();
            numero.setValeurNumero(dto.getValeurNumero());
            numero = numeroService.createNumero(numero);
            logger.info("Numéro créé : {}", numero.getValeurNumero());
        }

        // 🔹 Vérifier et créer l'association User ↔ Numéro
        PossedeId id = new PossedeId(user.getId(), numero.getId());
        Possede savedPossede;

        if (possedeRepository.existsById(id)) {
            logger.info("Association utilisateur={} / numero={} déjà existante, réutilisation",
                        user.getUsername(), numero.getValeurNumero());
            savedPossede = possedeRepository.findById(id).get();
            messages.add("Association User ↔ Numéro déjà existante, réutilisation");
        } else {
            Possede possede = new Possede(user, numero);
            savedPossede = possedeRepository.save(possede);
            logger.info("Association créée : utilisateur={} / numero={}", 
                        user.getUsername(), numero.getValeurNumero());
        }

        // 🔹 Ajouter la disponibilité sur une plateforme si fournie
        if (dto.getIdPlateforme() != null) {
            DisponibleSurDTO dispoDTO = new DisponibleSurDTO();
            dispoDTO.setIdNumero(numero.getId());
            dispoDTO.setIdPlateforme(dto.getIdPlateforme());

            try {
                disponibleSurService.create(dispoDTO);
                String msg = "creeer";
                messages.add(msg);
            } catch (RuntimeException e) {
                String msg = "Disponibilité déjà existante pour numero=" 
                             + numero.getId() + " / plateforme=" + dto.getIdPlateforme();
                logger.info(msg + ", passage");
                messages.add(msg);
            }
        }

        // 🔹 Retourner la réponse avec messages éventuels
        PossedeResponseDTO response = new PossedeResponseDTO(savedPossede.getUtilisateur(), savedPossede.getNumero());
        response.setMessages(messages); // ← ajouter les messages à la réponse
        return response;
    }
}
