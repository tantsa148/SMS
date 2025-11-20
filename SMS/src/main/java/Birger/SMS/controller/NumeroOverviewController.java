package Birger.SMS.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.dto.NumeroCompletDTO;
import Birger.SMS.security.JwtUtil;
import Birger.SMS.service.NumeroOverviewService;

@RestController
@RequestMapping("/api/numeros-overview")
public class NumeroOverviewController {

    private final NumeroOverviewService numeroOverviewService;

    public NumeroOverviewController(NumeroOverviewService numeroOverviewService) {
        this.numeroOverviewService = numeroOverviewService;
    }

    /**
     * Récupère tous les numéros avec leurs propriétaires et plateformes
     * (Nécessite des privilèges admin)
     */
    @GetMapping
    public ResponseEntity<List<NumeroCompletDTO>> getAllNumerosWithDetails(
            @RequestHeader("Authorization") String token) {
        
        Long userId = JwtUtil.extractUserId(token);
        // Ici vous pourriez vérifier si l'utilisateur a les droits admin
        // Pour l'instant, on retourne tous les numéros
        
        return ResponseEntity.ok(numeroOverviewService.getAllNumerosWithDetails());
    }

    /**
     * Récupère les numéros de l'utilisateur connecté avec leurs plateformes
     */
    @GetMapping("/mes-numeros")
    public ResponseEntity<List<NumeroCompletDTO>> getMyNumerosWithDetails(
            @RequestHeader("Authorization") String token) {
        
        Long userId = JwtUtil.extractUserId(token);
        return ResponseEntity.ok(numeroOverviewService.getNumerosByUserWithDetails(userId));
    }

    /**
     * Récupère les numéros de l'utilisateur connecté disponibles sur une plateforme spécifique
     */
    @GetMapping("/mes-numeros/plateforme/{plateformeId}")
    public ResponseEntity<List<NumeroCompletDTO>> getMyNumerosByPlateforme(
            @RequestHeader("Authorization") String token,
            @PathVariable Long plateformeId) {
        
        Long userId = JwtUtil.extractUserId(token);
        return ResponseEntity.ok(numeroOverviewService.getNumerosByUserAndPlateforme(userId, plateformeId));
    }

    /**
     * Récupère les numéros disponibles sur une plateforme spécifique
     * (Accessible à tous les utilisateurs authentifiés)
     */
    @GetMapping("/plateforme/{plateformeId}")
    public ResponseEntity<List<NumeroCompletDTO>> getNumerosByPlateforme(
            @RequestHeader("Authorization") String token,
            @PathVariable Long plateformeId) {
        
        // On extrait juste pour valider le token, mais on n'utilise pas l'ID
        JwtUtil.extractUserId(token);
        return ResponseEntity.ok(numeroOverviewService.getNumerosByPlateformeWithDetails(plateformeId));
    }
}