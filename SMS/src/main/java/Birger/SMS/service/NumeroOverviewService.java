package Birger.SMS.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import Birger.SMS.dto.NumeroCompletDTO;
import Birger.SMS.dto.PossedeResponseDTO;
import Birger.SMS.model.Numero;
import Birger.SMS.repository.NumeroRepository;

@Service
public class NumeroOverviewService {

    private static final Logger logger = LoggerFactory.getLogger(NumeroOverviewService.class);

    private final NumeroRepository numeroRepository;
    private final PossedeService possedeService;
    private final DisponibleSurService disponibleSurService;

    public NumeroOverviewService(NumeroRepository numeroRepository,
                                PossedeService possedeService,
                                DisponibleSurService disponibleSurService) {
        this.numeroRepository = numeroRepository;
        this.possedeService = possedeService;
        this.disponibleSurService = disponibleSurService;
    }

    /**
     * Récupère tous les numéros avec leurs propriétaires et plateformes
     * (Pour admin ou vue globale)
     */
    public List<NumeroCompletDTO> getAllNumerosWithDetails() {
        logger.info("Récupération de tous les numéros avec détails complets");
        
        return numeroRepository.findAll().stream()
                .map(this::mapToNumeroCompletDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les numéros d'un utilisateur spécifique avec leurs plateformes
     */
    public List<NumeroCompletDTO> getNumerosByUserWithDetails(Long userId) {
        logger.info("Récupération des numéros pour l'utilisateur {} avec plateformes", userId);
        
        // Utilise le service Possede existant pour récupérer les numéros de l'utilisateur
        List<PossedeResponseDTO> userNumbers = possedeService.getPossedeByUserId(userId);
        
        return userNumbers.stream()
                .map(possedeResponse -> {
                    NumeroCompletDTO dto = new NumeroCompletDTO();
                    dto.setId(possedeResponse.getIdNumero());
                    dto.setValeurNumero(possedeResponse.getValeurNumero());
                    dto.setProprietaire(possedeResponse.getUsername());
                    dto.setPlateformes(getPlateformesForNumero(possedeResponse.getIdNumero()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Récupère les numéros disponibles sur une plateforme spécifique avec leurs propriétaires
     */
    public List<NumeroCompletDTO> getNumerosByPlateformeWithDetails(Long plateformeId) {
        logger.info("Récupération des numéros disponibles sur la plateforme {}", plateformeId);
        
        // Utilise le service DisponibleSur existant
        return disponibleSurService.getAll().stream()
                .filter(disponible -> disponible.getIdPlateforme().equals(plateformeId))
                .map(disponible -> {
                    NumeroCompletDTO dto = new NumeroCompletDTO();
                    dto.setId(disponible.getIdNumero());
                    dto.setValeurNumero(disponible.getValeurNumero());
                    dto.setProprietaire(getProprietaireForNumero(disponible.getIdNumero()));
                    dto.setPlateformes(List.of(disponible.getNomPlateforme()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Récupère les numéros d'un utilisateur disponibles sur une plateforme spécifique
     */
    public List<NumeroCompletDTO> getNumerosByUserAndPlateforme(Long userId, Long plateformeId) {
        logger.info("Récupération des numéros pour user {} sur plateforme {}", userId, plateformeId);
        
        // Récupère d'abord tous les numéros de l'utilisateur
        List<NumeroCompletDTO> userNumbers = getNumerosByUserWithDetails(userId);
        
        // Filtre ceux qui sont disponibles sur la plateforme demandée
        return userNumbers.stream()
                .filter(numero -> numero.getPlateformes().stream()
                        .anyMatch(plateforme -> isPlateformeMatch(plateforme, plateformeId)))
                .collect(Collectors.toList());
    }

    // ==================== MÉTHODES PRIVÉES ====================

    private NumeroCompletDTO mapToNumeroCompletDTO(Numero numero) {
        NumeroCompletDTO dto = new NumeroCompletDTO();
        dto.setId(numero.getId());
        dto.setValeurNumero(numero.getValeurNumero());
        dto.setProprietaire(getProprietaireForNumero(numero.getId()));
        dto.setPlateformes(getPlateformesForNumero(numero.getId()));
        return dto;
    }

    private String getProprietaireForNumero(Long numeroId) {
        try {
            // Cette méthode nécessite d'être adaptée selon votre implémentation
            // Pour l'instant, on retourne une valeur par défaut
            return "Utilisateur";
        } catch (Exception e) {
            logger.warn("Erreur lors de la récupération du propriétaire pour le numéro {}", numeroId, e);
            return "Non assigné";
        }
    }

    private List<String> getPlateformesForNumero(Long numeroId) {
        try {
            // Utilise le service DisponibleSur pour trouver les plateformes
            return disponibleSurService.getAll().stream()
                    .filter(disponible -> disponible.getIdNumero().equals(numeroId))
                    .map(disponible -> disponible.getNomPlateforme())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.warn("Erreur lors de la récupération des plateformes pour le numéro {}", numeroId, e);
            return List.of();
        }
    }

    private boolean isPlateformeMatch(String plateformeNom, Long plateformeId) {
        // Cette méthode devrait mapper le nom de la plateforme à son ID
        try {
            return disponibleSurService.getAll().stream()
                    .anyMatch(disponible -> 
                        disponible.getIdPlateforme().equals(plateformeId) && 
                        disponible.getNomPlateforme().equals(plateformeNom));
        } catch (Exception e) {
            return false;
        }
    }
}