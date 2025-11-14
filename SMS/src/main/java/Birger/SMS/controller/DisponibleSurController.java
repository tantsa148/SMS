package Birger.SMS.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.dto.DisponibleSurDTO;
import Birger.SMS.dto.DisponibleSurResponseDTO;
import Birger.SMS.service.DisponibleSurService;

@RestController
@RequestMapping("/api/disponible-sur")
public class DisponibleSurController {

    private final DisponibleSurService disponibleSurService;

    public DisponibleSurController(DisponibleSurService disponibleSurService) {
        this.disponibleSurService = disponibleSurService;
    }

    // -------------------- GET ALL --------------------
    @GetMapping
    public ResponseEntity<List<DisponibleSurResponseDTO>> getAll() {
        List<DisponibleSurResponseDTO> list = disponibleSurService.getAll();
        return ResponseEntity.ok(list);
    }

    // -------------------- CREATE --------------------
    @PostMapping
    public ResponseEntity<?> create(@RequestBody DisponibleSurDTO dto) {
        try {
            DisponibleSurResponseDTO created = disponibleSurService.create(dto);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Erreur lors de la création", e.getMessage())
            );
        }
    }

    // -------------------- DELETE --------------------
    @DeleteMapping("/{idNumero}/{idPlateforme}")
    public ResponseEntity<?> delete(@PathVariable Long idNumero, @PathVariable Long idPlateforme) {
        try {
            disponibleSurService.delete(idNumero, idPlateforme);
            return ResponseEntity.ok(
                    new MessageResponse("Association supprimée avec succès.")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Erreur lors de la suppression", e.getMessage())
            );
        }
    }

    // -------------------- Classes utilitaires pour messages JSON --------------------
    public static class ErrorResponse {
        public String message;
        public String details;

        public ErrorResponse(String message, String details) {
            this.message = message;
            this.details = details;
        }
    }

    public static class MessageResponse {
        public String message;

        public MessageResponse(String message) {
            this.message = message;
        }
    }
}
