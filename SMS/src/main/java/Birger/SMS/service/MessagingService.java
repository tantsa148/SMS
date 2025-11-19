package Birger.SMS.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import Birger.SMS.dto.PossedeResponseDTO;
import Birger.SMS.dto.SendMessageResponseDTO;
import Birger.SMS.model.SMS;
import Birger.SMS.utils.TwilioErrorHandler;

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
    public SendMessageResponseDTO envoyerSMSAvecUserId(Long userId, SMS sms) {
        List<PossedeResponseDTO> numeros = numeroAssignService.getNumerosByUserId(userId);
        if (numeros.isEmpty()) {
            throw new RuntimeException("Aucun numéro associé à cet utilisateur");
        }
        sms.setExpediteur(numeros.get(0).getValeurNumero());
        return envoyerMessage(sms, false);
    }

    public SendMessageResponseDTO envoyerSMSAvecUserIdEtNumero(Long userId, Long idNumero, SMS sms) {
        PossedeResponseDTO numeroChoisi = numeroAssignService.getNumerosByUserId(userId).stream()
                .filter(n -> n.getIdNumero().equals(idNumero))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Le numéro choisi n'appartient pas à l'utilisateur id=" + userId));

        sms.setExpediteur(numeroChoisi.getValeurNumero());
        return envoyerMessage(sms, false);
    }

    // ------------------ WhatsApp ------------------
    public SendMessageResponseDTO envoyerWhatsAppAvecUserId(Long userId, SMS sms) {
        List<PossedeResponseDTO> numeros = numeroAssignService.getNumerosByUserId(userId);
        if (numeros.isEmpty()) {
            throw new RuntimeException("Aucun numéro associé à cet utilisateur");
        }
        sms.setExpediteur(numeros.get(0).getValeurNumero());
        return envoyerMessage(sms, true);
    }

    public SendMessageResponseDTO envoyerWhatsAppAvecUserIdEtNumero(Long userId, Long idNumero, SMS sms) {
        PossedeResponseDTO numeroChoisi = numeroAssignService.getNumerosByUserId(userId).stream()
                .filter(n -> n.getIdNumero().equals(idNumero))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Le numéro choisi n'appartient pas à l'utilisateur id=" + userId));

        sms.setExpediteur(numeroChoisi.getValeurNumero());
        return envoyerMessage(sms, true);
    }

    // ------------------ Méthode interne ------------------
    private SendMessageResponseDTO envoyerMessage(SMS sms, boolean isWhatsapp) {
        try {
            if (sms.getDestinataire() == null || sms.getDestinataire().isBlank()) {
                throw new IllegalArgumentException("Le numéro du destinataire est manquant ou vide.");
            }
            if (sms.getExpediteur() == null || sms.getExpediteur().isBlank()) {
                throw new IllegalArgumentException("Le numéro de l'expéditeur est manquant ou vide.");
            }

            Twilio.init(accountSid, authToken);

            String to   = isWhatsapp ? "whatsapp:" + sms.getDestinataire() : sms.getDestinataire();
            String from = isWhatsapp ? "whatsapp:" + sms.getExpediteur()   : sms.getExpediteur();

            Message twilioMsg = Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(from),
                    sms.getMessageTexte().getContenu()
            ).create();

            logger.info("Twilio {} envoyé – SID: {}, status: {}, errorCode: {}",
                    isWhatsapp ? "WhatsApp" : "SMS",
                    twilioMsg.getSid(),
                    twilioMsg.getStatus(),
                    twilioMsg.getErrorCode());

            return new SendMessageResponseDTO(
                    sms.getExpediteur(),
                    sms.getDestinataire(),
                    sms.getMessageTexte().getContenu(),
                    twilioMsg.getStatus() != null ? twilioMsg.getStatus().toString() : "UNKNOWN",
                    twilioMsg.getErrorCode(),
                    twilioMsg.getErrorMessage(),
                    twilioMsg.getSid(),
                    twilioMsg.getDateCreated(),
                    isWhatsapp ? "whatsapp" : "sms"
            );

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi du message via Twilio", e);
            // Tu peux aussi créer un DTO d'erreur dédié si tu veux plus de granularité
            var errorMap = TwilioErrorHandler.handleException(e);

            return new SendMessageResponseDTO(
                    sms.getExpediteur(),
                    sms.getDestinataire(),
                    sms.getMessageTexte().getContenu(),
                    "FAILED",
                    (Integer) errorMap.get("errorCode"),
                    (String) errorMap.getOrDefault("errorMessage", e.getMessage()),
                    null,
                    null,
                    isWhatsapp ? "whatsapp" : "sms"
            );
        }
    }
}