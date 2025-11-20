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
import Birger.SMS.model.SMS;

@Service
public class MessagingService {

    private static final Logger logger = LoggerFactory.getLogger(MessagingService.class);

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    private final NumeroAssignService numeroAssignService;

    public MessagingService(NumeroAssignService numeroAssignService) {
        this.numeroAssignService = numeroAssignService;
    }

    // ------------------ SMS ------------------
    public SendMessageDBResponseDTO envoyerSMSAvecUserId(Long userId, SMS sms) {
        List<PossedeResponseDTO> numeros = numeroAssignService.getNumerosByUserId(userId);
        if (numeros.isEmpty()) {
            throw new RuntimeException("Aucun numéro associé à cet utilisateur");
        }
        sms.setExpediteur(numeros.get(0).getValeurNumero());
        return envoyerMessage(sms, false);
    }

    public SendMessageDBResponseDTO envoyerSMSAvecUserIdEtNumero(Long userId, Long idNumero, SMS sms) {
        PossedeResponseDTO numeroChoisi = numeroAssignService.getNumerosByUserId(userId).stream()
                .filter(n -> n.getIdNumero().equals(idNumero))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Le numéro choisi n'appartient pas à l'utilisateur id=" + userId));
        sms.setExpediteur(numeroChoisi.getValeurNumero());
        return envoyerMessage(sms, false);
    }

    // ------------------ WhatsApp ------------------
    public SendMessageDBResponseDTO envoyerWhatsAppAvecUserId(Long userId, SMS sms) {
        List<PossedeResponseDTO> numeros = numeroAssignService.getNumerosByUserId(userId);
        if (numeros.isEmpty()) {
            throw new RuntimeException("Aucun numéro associé à cet utilisateur");
        }
        sms.setExpediteur(numeros.get(0).getValeurNumero());
        return envoyerMessage(sms, true);
    }

    public SendMessageDBResponseDTO envoyerWhatsAppAvecUserIdEtNumero(Long userId, Long idNumero, SMS sms) {
        PossedeResponseDTO numeroChoisi = numeroAssignService.getNumerosByUserId(userId).stream()
                .filter(n -> n.getIdNumero().equals(idNumero))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Le numéro choisi n'appartient pas à l'utilisateur id=" + userId));
        sms.setExpediteur(numeroChoisi.getValeurNumero());
        return envoyerMessage(sms, true);
    }

    // ------------------ Méthode interne ------------------
    private SendMessageDBResponseDTO envoyerMessage(SMS sms, boolean isWhatsapp) {
        try {
            if (sms.getDestinataire() == null || sms.getDestinataire().isBlank()) {
                throw new IllegalArgumentException("Numéro destinataire manquant ou vide.");
            }
            if (sms.getExpediteur() == null || sms.getExpediteur().isBlank()) {
                throw new IllegalArgumentException("Numéro expéditeur manquant ou vide.");
            }

            Twilio.init(accountSid, authToken);

            String to   = isWhatsapp ? "whatsapp:" + sms.getDestinataire() : sms.getDestinataire();
            String from = isWhatsapp ? "whatsapp:" + sms.getExpediteur()   : sms.getExpediteur();

            // Création du message Twilio
            com.twilio.rest.api.v2010.account.Message twilioMsg =
                    com.twilio.rest.api.v2010.account.Message.creator(
                            new PhoneNumber(to),
                            new PhoneNumber(from),
                            sms.getMessageTexte().getContenu()
                    ).create();

            // Récupérer le statut final du message via SID
            com.twilio.rest.api.v2010.account.Message fetchedMsg =
                    com.twilio.rest.api.v2010.account.Message.fetcher(twilioMsg.getSid()).fetch();

            Status finalStatus = fetchedMsg.getStatus(); // statut final réel
            Integer errorCode = fetchedMsg.getErrorCode();
            String errorMessage = fetchedMsg.getErrorMessage();

            logger.info("Message envoyé → SID: {}, statut final: {}", fetchedMsg.getSid(), finalStatus);

            Date dateCreated = fetchedMsg.getDateCreated() != null
                    ? Date.from(fetchedMsg.getDateCreated().toInstant())
                    : null;

            return new SendMessageDBResponseDTO(
                    errorCode == null ? "success" : "failed",
                    finalStatus != null ? finalStatus.toString() : "unknown",
                    errorCode,
                    errorMessage,
                    isWhatsapp ? "whatsapp" : "sms",
                    null, // messageId inexistant ici
                    sms.getDestinataire(),
                    sms.getExpediteur(),
                    sms.getMessageTexte().getContenu(),
                    fetchedMsg.getSid(),
                    dateCreated
            );

        } catch (Exception e) {
            logger.error("Échec envoi message → destinataire={}", sms.getDestinataire(), e);

            return new SendMessageDBResponseDTO(
                    "failed",
                    "error",
                    400,
                    e.getMessage(),
                    isWhatsapp ? "whatsapp" : "sms",
                    null,
                    sms.getDestinataire(),
                    sms.getExpediteur(),
                    sms.getMessageTexte() != null ? sms.getMessageTexte().getContenu() : null,
                    null,
                    null
            );
        }
    }
}
