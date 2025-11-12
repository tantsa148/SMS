package Birger.SMS.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.model.SMS;
import Birger.SMS.service.MessagingService;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    private final MessagingService messagingService;

    public WhatsAppController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    /**
     * Envoie un message WhatsApp
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> envoyerWhatsApp(@RequestBody SMS sms) {
        try {
            Map<String, Object> response = messagingService.envoyerWhatsApp(sms);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}
