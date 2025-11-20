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
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    private final MessagingService messagingService;

    public WhatsAppController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @PostMapping("/send")
    public ResponseEntity<SendMessageDBResponseDTO> envoyerWhatsApp(
            @RequestBody SMS sms,
            HttpServletRequest request) {

        Long userId = extractUserIdFromToken(request);

        SendMessageDBResponseDTO response = sms.getIdNumero() != null
                ? messagingService.envoyerWhatsAppAvecUserIdEtNumero(userId, sms.getIdNumero(), sms)
                : messagingService.envoyerWhatsAppAvecUserId(userId, sms);

        return ResponseEntity.ok(response);
    }

    // ────────────────────────────────────────────────
    // Méthode privée pour extraire le userId depuis JWT
    // ────────────────────────────────────────────────
    private Long extractUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token JWT manquant ou invalide");
        }
        String token = authHeader.substring(7);
        return JwtUtil.extractUserId(token); // lève exception si invalide
    }

    // ────────────────────────────────────────────────
    // Gestion centralisée des erreurs
    // ────────────────────────────────────────────────
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("error", ex.getMessage()));
    }

    // DTO simple pour les erreurs
    record ErrorResponse(String status, String message) {}
}
