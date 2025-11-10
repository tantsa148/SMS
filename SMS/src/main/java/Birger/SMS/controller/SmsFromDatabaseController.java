package Birger.SMS.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.service.SmsFromDatabaseService;

@RestController
@RequestMapping("/api/sms-db")
public class SmsFromDatabaseController {

    private final SmsFromDatabaseService smsFromDatabaseService;

    public SmsFromDatabaseController(SmsFromDatabaseService smsFromDatabaseService) {
        this.smsFromDatabaseService = smsFromDatabaseService;
    }

    /**
     * Envoie un message depuis la base via Twilio
     *
     * Corps JSON attendu :
     * {
     *   "messageId": 1,
     *   "destinataire": "+261340000000",
     *   "expediteur": "+261330000000"
     * }
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> envoyerDepuisBase(@RequestBody Map<String, Object> requestData) {
        try {
            // Récupération des données depuis le JSON
            Long messageId = Long.valueOf(requestData.get("messageId").toString());
            String destinataire = requestData.get("destinataire").toString();
            String expediteur = requestData.get("expediteur").toString();

            // Appel du service
            Map<String, Object> response = smsFromDatabaseService.envoyerMessageDepuisBase(
                    messageId, destinataire, expediteur);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Gestion des erreurs et réponse JSON
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}
