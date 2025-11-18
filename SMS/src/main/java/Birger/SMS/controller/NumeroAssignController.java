package Birger.SMS.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.dto.PossedeDTO;
import Birger.SMS.dto.PossedeResponseDTO;
import Birger.SMS.security.JwtUtil;
import Birger.SMS.service.NumeroAssignService;

@RestController
@RequestMapping("/api/numero-assign")
public class NumeroAssignController {

    private final NumeroAssignService numeroAssignService;

    public NumeroAssignController(NumeroAssignService numeroAssignService) {
        this.numeroAssignService = numeroAssignService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNumeroAndPossede(@RequestBody PossedeDTO dto,
                                                    @RequestHeader("Authorization") String authHeader) {
        try {
            // 1️⃣ Récupérer ID utilisateur depuis JWT
            Long userId = JwtUtil.extractUserId(authHeader);
            dto.setIdUtilisateur(userId);

            // 2️⃣ Créer numéro + association
            PossedeResponseDTO response = numeroAssignService.createNumeroAndPossede(dto);

            // 3️⃣ Retour OK
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Erreur lors de la création : " + e.getMessage());
        }
    }
}
