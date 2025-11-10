package Birger.SMS.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;

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

    // Envoyer SMS classique
        public Map<String, Object> envoyerMessageDepuisBase(Long messageId, String destinataire, String expediteur) {
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

                com.twilio.rest.api.v2010.account.Message twilioMsg =
                        com.twilio.rest.api.v2010.account.Message.creator(
                                new com.twilio.type.PhoneNumber(destinataire),
                                new com.twilio.type.PhoneNumber(expediteur),
                                messageEnBase.getTexte()
                        ).create();

                response.put("status", "success");
                response.put("messageId", messageId);
                response.put("destinataire", destinataire);
                response.put("expediteur", expediteur);
                response.put("texte", messageEnBase.getTexte());
                response.put("sid", twilioMsg.getSid());
                response.put("dateCreated", twilioMsg.getDateCreated());
                response.put("statusMessage", twilioMsg.getStatus().toString());

                } catch (Exception e) {
                logger.error("Erreur lors de l'envoi SMS depuis la base : ", e);  // 🔥 log dans la console
                response = TwilioErrorHandler.handleException(e);
                }

                return response;
        }


    // Envoyer WhatsApp
        public Map<String, Object> envoyerWhatsappDepuisBase(Long messageId, String destinataire, String expediteur) {
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

                String whatsappDestinataire = "whatsapp:" + destinataire;
                String whatsappExpediteur = "whatsapp:" + expediteur;

                com.twilio.rest.api.v2010.account.Message twilioMsg =
                        com.twilio.rest.api.v2010.account.Message.creator(
                                new com.twilio.type.PhoneNumber(whatsappDestinataire),
                                new com.twilio.type.PhoneNumber(whatsappExpediteur),
                                messageEnBase.getTexte()
                        ).create();

                response.put("status", "success");
                response.put("type", "whatsapp");
                response.put("messageId", messageId);
                response.put("destinataire", destinataire);
                response.put("expediteur", expediteur);
                response.put("texte", messageEnBase.getTexte());
                response.put("sid", twilioMsg.getSid());
                response.put("dateCreated", twilioMsg.getDateCreated());
                response.put("statusMessage", twilioMsg.getStatus().toString());

                } catch (Exception e) {
                logger.error("Erreur lors de l'envoi WhatsApp depuis la base : ", e);  // 🔥 log dans la console
                response = TwilioErrorHandler.handleException(e);
                }

                return response;
        }
}
