package Birger.SMS.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message.Status;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

import Birger.SMS.model.Message;
import Birger.SMS.repository.MessageRepository;
import Birger.SMS.utils.TwilioErrorHandler;

@Service
public class SmsFromDatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(SmsFromDatabaseService.class);

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    private final MessageRepository messageRepository;

    public SmsFromDatabaseService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // 📨 Envoi SMS
    public Map<String, Object> envoyerMessageDepuisBase(Long messageId, String destinataire, String expediteur) {
        return envoyerDepuisBase(messageId, destinataire, expediteur, false);
    }

    // 💬 Envoi WhatsApp
    public Map<String, Object> envoyerWhatsappDepuisBase(Long messageId, String destinataire, String expediteur) {
        return envoyerDepuisBase(messageId, destinataire, expediteur, true);
    }

    /**
     * Méthode commune pour SMS et WhatsApp
     */
    private Map<String, Object> envoyerDepuisBase(Long messageId, String destinataire, String expediteur, boolean isWhatsapp) {
        Map<String, Object> response = new HashMap<>();
        try {
            Twilio.init(accountSid, authToken);

            Message messageEnBase = messageRepository.findById(messageId)
                    .orElseThrow(() -> new RuntimeException("Message introuvable avec id: " + messageId));

            if (destinataire == null || destinataire.isBlank()) {
                throw new IllegalArgumentException("Le numéro du destinataire est manquant ou vide.");
            }
            if (expediteur == null || expediteur.isBlank()) {
                throw new IllegalArgumentException("Le numéro de l'expéditeur est manquant ou vide.");
            }

            // Préfixer pour WhatsApp
            String to = isWhatsapp ? "whatsapp:" + destinataire : destinataire;
            String from = isWhatsapp ? "whatsapp:" + expediteur : expediteur;

            MessageCreator creator = com.twilio.rest.api.v2010.account.Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(from),
                    messageEnBase.getTexte()
            );

            com.twilio.rest.api.v2010.account.Message twilioMsg = creator.create();

            // 🔍 Vérification de l'état Twilio
            // Après twilioMsg.create()
                Status status = twilioMsg.getStatus();
                Integer errorCode = twilioMsg.getErrorCode();
                String errorMessage = twilioMsg.getErrorMessage();

                // Considérer comme succès si pas d'erreur Twilio
                boolean isSuccess = (errorCode == null); // ignore le statut DELIVERED ici

                if (!isSuccess) {
                response.put("status", "failed");
                response.put("statusMessage", status.toString());
                response.put("errorCode", errorCode);
                response.put("errorMessage", errorMessage != null ? errorMessage : "Erreur Twilio");
                } else {
                response.put("status", "success");
                response.put("statusMessage", status.toString());
                }


            // 🔧 Ajouter infos générales
            response.put("type", isWhatsapp ? "whatsapp" : "sms");
            response.put("messageId", messageId);
            response.put("destinataire", destinataire);
            response.put("expediteur", expediteur);
            response.put("texte", messageEnBase.getTexte());
            response.put("sid", twilioMsg.getSid());
            response.put("dateCreated", twilioMsg.getDateCreated());

            // 🪵 Log complet pour debug
            logger.info("Twilio message SID: {}, status: {}, errorCode: {}, errorMessage: {}",
                    twilioMsg.getSid(), status, errorCode, errorMessage);

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi du message via Twilio : ",e);
            response = TwilioErrorHandler.handleException(e);
        }

        return response;
    }
}
