package Birger.SMS.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.dto.SendMessageDBResponseDTO;
import Birger.SMS.model.SMS;
import Birger.SMS.security.JwtUtil;
import Birger.SMS.service.MessagingService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final MessagingService messagingService;

    public SmsController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @PostMapping("/send")
    public ResponseEntity<SendMessageDBResponseDTO> envoyerSms(
            @RequestBody SMS sms,
            HttpServletRequest request) {

        Long userId = extractUserIdFromToken(request);

        SendMessageDBResponseDTO response;

        if (sms.getIdNumero() != null) {
            // Envoi avec numéro choisi
            response = messagingService.envoyerSMSAvecUserIdEtNumero(userId, sms.getIdNumero(), sms);
        } else {
            // Envoi avec premier numéro disponible de l'utilisateur
            response = messagingService.envoyerSMSAvecUserId(userId, sms);
        }

        return ResponseEntity.ok(response);
    }

    // ────────────────────────────────────────────────
    // Méthode utilitaire pour extraire et valider le JWT
    // ────────────────────────────────────────────────
    private Long extractUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token JWT manquant ou invalide");
        }

        String token = authHeader.substring(7);
        return JwtUtil.extractUserId(token); // lève exception si token invalide
    }

    // ────────────────────────────────────────────────
    // Gestion globale des erreurs
    // ────────────────────────────────────────────────
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        var error = new ErrorResponse("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    // DTO simple pour les erreurs
    record ErrorResponse(String status, String message) {}
}
