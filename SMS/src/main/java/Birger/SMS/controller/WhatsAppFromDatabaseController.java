package Birger.SMS.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.dto.SendMessageDBResponseDTO;
import Birger.SMS.security.JwtUtil;
import Birger.SMS.service.SmsFromDatabaseService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/whatsapp-db")
public class WhatsAppFromDatabaseController {

    private final SmsFromDatabaseService service;

    public WhatsAppFromDatabaseController(SmsFromDatabaseService service) {
        this.service = service;
    }

    // ------------------------------------------------------------
    // 1. WhatsApp – numéro automatique
    // ------------------------------------------------------------
    @PostMapping("/send")
    public ResponseEntity<SendMessageDBResponseDTO> envoyerWhatsappAuto(
            @RequestBody Map<String, Object> request,
            HttpServletRequest httpRequest) {

        Long userId = extractUserIdFromToken(httpRequest);
        Long messageId = Long.valueOf(request.get("messageId").toString());
        String destinataire = (String) request.get("destinataire");

        return ResponseEntity.ok(service.envoyerWhatsappDepuisBaseAvecUserId(userId, messageId, destinataire));
    }

    // ------------------------------------------------------------
    // 2. WhatsApp – numéro choisi
    // ------------------------------------------------------------
    @PostMapping("/send/numero")
    public ResponseEntity<SendMessageDBResponseDTO> envoyerWhatsappAvecNumero(
            @RequestBody Map<String, Object> request,
            HttpServletRequest httpRequest) {

        Long userId = extractUserIdFromToken(httpRequest);
        Long idNumero = Long.valueOf(request.get("idNumero").toString());
        Long messageId = Long.valueOf(request.get("messageId").toString());
        String destinataire = (String) request.get("destinataire");

        return ResponseEntity.ok(service.envoyerWhatsappDepuisBaseAvecUserIdEtNumero(
                userId, idNumero, messageId, destinataire));
    }

    // ──────────────────────────────────────────────────────────────
    private Long extractUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token JWT manquant ou invalide");
        }
        String token = authHeader.substring(7);
        return JwtUtil.extractUserId(token);
    }
}
