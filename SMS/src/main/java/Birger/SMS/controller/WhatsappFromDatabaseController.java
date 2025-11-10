package Birger.SMS.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.service.SmsFromDatabaseService;

@RestController
@RequestMapping("/api/whatsapp-db")
public class WhatsappFromDatabaseController {

    private final SmsFromDatabaseService smsFromDatabaseService;

    public WhatsappFromDatabaseController(SmsFromDatabaseService smsFromDatabaseService) {
        this.smsFromDatabaseService = smsFromDatabaseService;
    }

    /**
     * Envoie un message WhatsApp depuis la base via Twilio
     *
     * Corps JSON attendu :
     * {
     *   "messageId": 1,
     *   "destinataire": "+261340000000",
     *   "expediteur": "+14155238886"
     * }
     *
     * ⚠️ L'expéditeur doit être le numéro WhatsApp Twilio (sandbox ou certifié)
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> envoyerWhatsappDepuisBase(@RequestBody Map<String, Object> requestData) {
        try {
            // Récupération des données depuis le JSON
            Long messageId = Long.valueOf(requestData.get("messageId").toString());
            String destinataire = requestData.get("destinataire").toString();
            String expediteur = requestData.get("expediteur").toString();

            // Appel du service pour envoi WhatsApp
            Map<String, Object> response = smsFromDatabaseService.envoyerWhatsappDepuisBase(
                    messageId, destinataire, expediteur);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Gestion des erreurs
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}
