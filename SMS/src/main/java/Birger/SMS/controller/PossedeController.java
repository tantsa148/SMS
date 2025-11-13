package Birger.SMS.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.dto.PossedeDTO;
import Birger.SMS.dto.PossedeResponseDTO;
import Birger.SMS.model.Possede;
import Birger.SMS.service.PossedeService;

@RestController
@RequestMapping("/api/possede")

public class PossedeController {

    private final PossedeService possedeService;

    public PossedeController(PossedeService possedeService) {
        this.possedeService = possedeService;
    }

    // 🔹 Récupérer toutes les relations utilisateur ↔ numéro
    @GetMapping
    public ResponseEntity<List<PossedeResponseDTO>> getAll() {
        List<Possede> possedes = possedeService.getAllPossede();
        // Convertit la liste d'entités en liste de DTO pour le JSON
        List<PossedeResponseDTO> response = possedes.stream()
                .map(p -> new PossedeResponseDTO(p.getUtilisateur(), p.getNumero()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // 🔹 Créer une nouvelle relation
    @PostMapping
    public ResponseEntity<PossedeResponseDTO> create(@RequestBody PossedeDTO dto) {
        PossedeResponseDTO response = possedeService.createPossede(dto);
        return ResponseEntity.ok(response);
    }

    // 🔹 Supprimer une relation
    @DeleteMapping("/{idUtilisateur}/{idNumero}")
    public ResponseEntity<String> delete(@PathVariable Long idUtilisateur, @PathVariable Long idNumero) {
        possedeService.deletePossede(idUtilisateur, idNumero);
        return ResponseEntity.ok("Relation supprimée avec succès !");
    }
}
