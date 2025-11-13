package Birger.SMS.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.exception.PlateformeNotFoundException;
import Birger.SMS.model.Plateforme;
import Birger.SMS.service.PlateformeService;

@RestController
@RequestMapping("/api/plateformes")
public class PlateformeController {

    private final PlateformeService service;

    public PlateformeController(PlateformeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Plateforme plateforme) {
        try {
            return ResponseEntity.ok(service.createPlateforme(plateforme.getNomPlateform()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping
    public List<Plateforme> getAll() {
        return service.getAllPlateformes();
    }

        @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Plateforme> plateforme = service.getPlateformeById(id);
        if (plateforme.isPresent()) {
            return ResponseEntity.ok(plateforme.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Plateforme non trouvée avec l'id " + id));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Plateforme plateforme) {
        try {
            return ResponseEntity.ok(service.updatePlateforme(id, plateforme.getNomPlateform()));
        } catch (PlateformeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deletePlateforme(id);
            return ResponseEntity.noContent().build();
        } catch (PlateformeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // Classe interne pour formater les erreurs en JSON
    static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
