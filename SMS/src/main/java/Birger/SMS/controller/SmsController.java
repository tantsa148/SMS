package Birger.SMS.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * Envoie un SMS ou WhatsApp selon le champ isWhatsapp dans le SMS
     * Le numéro expéditeur est choisi selon idNumero si fourni, sinon premier numéro disponible
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> envoyerSms(@RequestBody SMS sms, HttpServletRequest request) {
        try {
            // 1️⃣ Récupérer token JWT
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Token invalide ou manquant");
            }
            String token = authHeader.substring(7);
            Long userId = JwtUtil.extractUserId(token);

            Map<String, Object> response;

            // 2️⃣ Choisir la méthode selon idNumero
            if (sms.getIdNumero() != null) {
                response = messagingService.envoyerSMSAvecUserIdEtNumero(userId, sms.getIdNumero(), sms);
            } else {
                response = messagingService.envoyerSMSAvecUserId(userId, sms);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}
