package Birger.SMS.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.dto.PossedeDTO;
import Birger.SMS.dto.PossedeResponseDTO;
import Birger.SMS.security.JwtUtil;
import Birger.SMS.service.NumeroAssignService;
import jakarta.servlet.http.HttpServletRequest;

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
        @GetMapping("/numeros")
        public List<PossedeResponseDTO> getNumeros(HttpServletRequest request) {

            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Token invalide ou manquant");
            }

            String token = authHeader.substring(7);

            Long userId = JwtUtil.extractUserId(token);

            return numeroAssignService.getNumerosByUserId(userId);
        }
}
