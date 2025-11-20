package Birger.SMS.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message.Status;
import com.twilio.type.PhoneNumber;

import Birger.SMS.dto.PossedeResponseDTO;
import Birger.SMS.dto.SendMessageDBResponseDTO;
import Birger.SMS.model.Message;
import Birger.SMS.repository.MessageRepository;

@Service
public class SmsFromDatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(SmsFromDatabaseService.class);

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    private final MessageRepository messageRepository;
    private final NumeroAssignService numeroAssignService;

    public SmsFromDatabaseService(MessageRepository messageRepository,
                                  NumeroAssignService numeroAssignService) {
        this.messageRepository = messageRepository;
        this.numeroAssignService = numeroAssignService;
    }

    // ------------------------------------------------------------
    // SMS - numéro automatique
    // ------------------------------------------------------------
    public SendMessageDBResponseDTO envoyerMessageDepuisBaseAvecUserId(
            Long userId, Long messageId, String destinataire) {

        List<PossedeResponseDTO> numeros = numeroAssignService.getNumerosByUserId(userId);

        if (numeros.isEmpty()) {
            throw new RuntimeException("Aucun numéro associé à cet utilisateur");
        }

        String expediteur = numeros.get(0).getValeurNumero();

        return envoyerDepuisBase(messageId, destinataire, expediteur, false);
    }

    // ------------------------------------------------------------
    // SMS - numéro choisi
    // ------------------------------------------------------------
    public SendMessageDBResponseDTO envoyerMessageDepuisBaseAvecUserIdEtNumero(
            Long userId, Long idNumero, Long messageId, String destinataire) {

        PossedeResponseDTO numeroChoisi = numeroAssignService.getNumerosByUserId(userId)
                .stream()
                .filter(n -> n.getIdNumero().equals(idNumero))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Le numéro choisi n'appartient pas à l'utilisateur id=" + userId));

        String expediteur = numeroChoisi.getValeurNumero();

        return envoyerDepuisBase(messageId, destinataire, expediteur, false);
    }

    // ------------------------------------------------------------
    // WhatsApp - numéro automatique
    // ------------------------------------------------------------
    public SendMessageDBResponseDTO envoyerWhatsappDepuisBaseAvecUserId(
            Long userId, Long messageId, String destinataire) {

        List<PossedeResponseDTO> numeros = numeroAssignService.getNumerosByUserId(userId);

        if (numeros.isEmpty()) {
            throw new RuntimeException("Aucun numéro associé à cet utilisateur");
        }

        String expediteur = numeros.get(0).getValeurNumero();

        return envoyerDepuisBase(messageId, destinataire, expediteur, true);
    }

    // ------------------------------------------------------------
    // WhatsApp - numéro choisi
    // ------------------------------------------------------------
    public SendMessageDBResponseDTO envoyerWhatsappDepuisBaseAvecUserIdEtNumero(
            Long userId, Long idNumero, Long messageId, String destinataire) {

        PossedeResponseDTO numeroChoisi = numeroAssignService.getNumerosByUserId(userId)
                .stream()
                .filter(n -> n.getIdNumero().equals(idNumero))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Le numéro choisi n'appartient pas à l'utilisateur id=" + userId));

        String expediteur = numeroChoisi.getValeurNumero();

        return envoyerDepuisBase(messageId, destinataire, expediteur, true);
    }

    // ------------------------------------------------------------
    // MÉTHODE COMMUNE : SMS + WhatsApp
    // ------------------------------------------------------------
private SendMessageDBResponseDTO envoyerDepuisBase(
        Long messageId,
        String destinataire,
        String expediteur,
        boolean isWhatsapp) {

    try {
        Twilio.init(accountSid, authToken);

        Message messageEnBase = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message introuvable avec id: " + messageId));

        if (destinataire == null || destinataire.isBlank()) {
            throw new IllegalArgumentException("Numéro destinataire manquant ou vide.");
        }

        if (expediteur == null || expediteur.isBlank()) {
            throw new IllegalArgumentException("Numéro expéditeur manquant ou vide.");
        }

        String to = isWhatsapp ? "whatsapp:" + destinataire : destinataire;
        String from = isWhatsapp ? "whatsapp:" + expediteur : expediteur;

        // Création du message
        com.twilio.rest.api.v2010.account.Message twilioMsg =
                com.twilio.rest.api.v2010.account.Message.creator(
                        new PhoneNumber(to),
                        new PhoneNumber(from),
                        messageEnBase.getTexte()
                ).create();

        // ------------------------------------------------------------
        // Récupérer le statut final via SID
        // ------------------------------------------------------------
        com.twilio.rest.api.v2010.account.Message fetchedMsg =
                com.twilio.rest.api.v2010.account.Message.fetcher(twilioMsg.getSid()).fetch();

        Status finalStatus = fetchedMsg.getStatus(); // vrai statut final
        Integer errorCode = fetchedMsg.getErrorCode();
        String errorMessage = fetchedMsg.getErrorMessage();

        logger.info("Message envoyé → SID: {}, statut final: {}", fetchedMsg.getSid(), finalStatus);

        // Conversion ZonedDateTime → Date
        Date dateCreated = fetchedMsg.getDateCreated() != null
                ? Date.from(fetchedMsg.getDateCreated().toInstant())
                : null;

        return new SendMessageDBResponseDTO(
                errorCode == null ? "success" : "failed",
                finalStatus != null ? finalStatus.toString() : "unknown",
                errorCode,
                errorMessage,
                isWhatsapp ? "whatsapp" : "sms",
                messageId,
                destinataire,
                expediteur,
                messageEnBase.getTexte(),
                fetchedMsg.getSid(),
                dateCreated
        );

    } catch (Exception e) {

        logger.error("Échec envoi message (base) → messageId={}", messageId, e);

        return new SendMessageDBResponseDTO(
                "failed",
                "error",
                400,
                e.getMessage(),
                isWhatsapp ? "whatsapp" : "sms",
                messageId,
                destinataire,
                expediteur,
                null,
                null,
                null
        );
    }
}
}
