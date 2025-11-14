package Birger.SMS.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
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

import Birger.SMS.model.Numero;
import Birger.SMS.service.NumeroService;

@RestController
@RequestMapping("/api/numeros")
public class NumeroController {

    private final NumeroService numeroService;

    public NumeroController(NumeroService numeroService) {
        this.numeroService = numeroService;
    }

    // ---------------- GET all ----------------
    @GetMapping
    public ResponseEntity<List<Numero>> getAllNumeros() {
        List<Numero> numeros = numeroService.getAllNumeros();
        return ResponseEntity.ok(numeros);
    }

    // ---------------- GET by ID ----------------
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getNumeroById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        return numeroService.getNumeroById(id)
                .map(numero -> {
                    response.put("success", true);
                    response.put("data", numero);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.put("success", false);
                    response.put("message", "Numéro non trouvé");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    // ---------------- CREATE ----------------
    @PostMapping
    public ResponseEntity<Map<String, Object>> createNumero(@RequestBody Numero numero) {
        Map<String, Object> response = new HashMap<>();
        try {
            Numero created = numeroService.createNumero(numero);
            response.put("success", true);
            response.put("data", created);
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            response.put("success", false);
            response.put("message", "Erreur : le numéro existe déjà !");
            return ResponseEntity.badRequest().body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur interne");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ---------------- UPDATE ----------------
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateNumero(@PathVariable Long id, @RequestBody Numero numero) {
        Map<String, Object> response = new HashMap<>();
        try {
            Numero updated = numeroService.updateNumero(id, numero);
            response.put("success", true);
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            response.put("success", false);
            response.put("message", "Erreur : ce numéro existe déjà !");
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur interne");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteNumero(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            numeroService.deleteNumero(id);
            response.put("success", true);
            response.put("message", "Numéro supprimé avec succès !");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur interne");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
