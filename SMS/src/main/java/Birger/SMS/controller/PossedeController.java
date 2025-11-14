package Birger.SMS.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.dto.PossedeDTO;
import Birger.SMS.dto.PossedeResponseDTO;
import Birger.SMS.security.JwtUtil;
import Birger.SMS.service.PossedeService;

@RestController
@RequestMapping("/api/possede")
public class PossedeController {

    private final PossedeService possedeService;

    public PossedeController(PossedeService possedeService) {
        this.possedeService = possedeService;
    }

    // ---------------- GET possessions de l'utilisateur connecté ----------------
    @GetMapping
    public ResponseEntity<List<PossedeResponseDTO>> getUserPossede(
            @RequestHeader("Authorization") String authHeader) {
        try {
            Long userId = JwtUtil.extractUserId(authHeader); // récupère l'ID depuis le JWT
            List<PossedeResponseDTO> possedes = possedeService.getPossedeByUserId(userId);
            return ResponseEntity.ok(possedes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ---------------- CREATE ----------------
    @PostMapping
    public ResponseEntity<?> createPossede(@RequestBody PossedeDTO dto,
                                           @RequestHeader("Authorization") String authHeader) {
        try {
            Long userId = JwtUtil.extractUserId(authHeader); // récupère l'ID depuis le JWT
            dto.setIdUtilisateur(userId);

            PossedeResponseDTO response = possedeService.createPossede(dto);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création : " + e.getMessage());
        }
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{idNumero}")
    public ResponseEntity<?> deletePossede(@PathVariable Long idNumero,
                                           @RequestHeader("Authorization") String authHeader) {
        try {
            Long userId = JwtUtil.extractUserId(authHeader); // récupère l'ID depuis le JWT
            possedeService.deletePossede(userId, idNumero);
            return ResponseEntity.ok("Possession supprimée avec succès !");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la suppression : " + e.getMessage());
        }
    }
}
