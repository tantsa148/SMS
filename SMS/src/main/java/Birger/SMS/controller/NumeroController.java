package Birger.SMS.controller;


import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.model.Numero;
import Birger.SMS.service.NumeroService;

@RestController
@RequestMapping("/api/numeros")

public class NumeroController {

    private final NumeroService numeroService;

    public NumeroController(NumeroService numeroService) {
        this.numeroService = numeroService;
    }

    @GetMapping
    public List<Numero> getAllNumeros() {
        return numeroService.getAllNumeros();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Numero> getNumeroById(@PathVariable Long id) {
        return numeroService.getNumeroById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createNumero(@RequestBody Numero numero) {
        try {
            Numero created = numeroService.createNumero(numero);
            return ResponseEntity.ok(created);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Erreur : le numéro existe déjà !");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la création du numéro.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNumero(@PathVariable Long id, @RequestBody Numero numero) {
        try {
            Numero updated = numeroService.updateNumero(id, numero);
            return ResponseEntity.ok(updated);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Erreur : ce numéro existe déjà !");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNumero(@PathVariable Long id) {
        numeroService.deleteNumero(id);
        return ResponseEntity.noContent().build();
    }
}
