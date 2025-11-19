package Birger.SMS.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.Message.Status;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

import Birger.SMS.dto.PossedeResponseDTO;
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

    public Map<String, Object> envoyerSMSAvecUserId(Long userId, SMS sms) {
        List<PossedeResponseDTO> numeros = numeroAssignService.getNumerosByUserId(userId);

        if (numeros.isEmpty()) {
            throw new RuntimeException("Aucun numéro associé à cet utilisateur");
        }

        // Utiliser le premier numéro assigné à l'utilisateur
        PossedeResponseDTO numeroChoisi = numeros.get(0);
        sms.setExpediteur(numeroChoisi.getValeurNumero());

        return envoyerMessage(sms, false);
    }

    public Map<String, Object> envoyerSMSAvecUserIdEtNumero(Long userId, Long idNumero, SMS sms) {
        List<PossedeResponseDTO> numeros = numeroAssignService.getNumerosByUserId(userId);

        PossedeResponseDTO numeroChoisi = numeros.stream()
                .filter(n -> n.getIdNumero().equals(idNumero))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Le numéro choisi n'appartient pas à l'utilisateur id=" + userId));

        // Remplacer le champ expediteur par le numéro réel
        sms.setExpediteur(numeroChoisi.getValeurNumero());

        return envoyerMessage(sms, false);
    }

    // ------------------ WhatsApp ------------------

    public Map<String, Object> envoyerWhatsAppAvecUserId(Long userId, SMS sms) {
        List<PossedeResponseDTO> numeros = numeroAssignService.getNumerosByUserId(userId);

        if (numeros.isEmpty()) {
            throw new RuntimeException("Aucun numéro associé à cet utilisateur");
        }

        PossedeResponseDTO numeroChoisi = numeros.get(0);
        sms.setExpediteur(numeroChoisi.getValeurNumero());

        return envoyerMessage(sms, true);
    }

    public Map<String, Object> envoyerWhatsAppAvecUserIdEtNumero(Long userId, Long idNumero, SMS sms) {
        List<PossedeResponseDTO> numeros = numeroAssignService.getNumerosByUserId(userId);

        PossedeResponseDTO numeroChoisi = numeros.stream()
                .filter(n -> n.getIdNumero().equals(idNumero))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Le numéro choisi n'appartient pas à l'utilisateur id=" + userId));

        sms.setExpediteur(numeroChoisi.getValeurNumero());

        return envoyerMessage(sms, true);
    }

    // ------------------ Méthode interne ------------------

    private Map<String, Object> envoyerMessage(SMS sms, boolean isWhatsapp) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (sms.getDestinataire() == null || sms.getDestinataire().isBlank()) {
                throw new IllegalArgumentException("Le numéro du destinataire est manquant ou vide.");
            }
            if (sms.getExpediteur() == null || sms.getExpediteur().isBlank()) {
                throw new IllegalArgumentException("Le numéro de l'expéditeur est manquant ou vide.");
            }

            Twilio.init(accountSid, authToken);

            String to = isWhatsapp ? "whatsapp:" + sms.getDestinataire() : sms.getDestinataire();
            String from = isWhatsapp ? "whatsapp:" + sms.getExpediteur() : sms.getExpediteur();

            MessageCreator creator = Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(from),
                    sms.getMessageTexte().getContenu()
            );

            Message twilioMsg = creator.create();

            Status status = twilioMsg.getStatus();
            Integer errorCode = twilioMsg.getErrorCode();
            String errorMessage = twilioMsg.getErrorMessage();

            boolean isSuccess = (errorCode == null);

            if (!isSuccess) {
                response.put("status", "failed");
                response.put("statusMessage", status.toString());
                response.put("errorCode", errorCode);
                response.put("errorMessage", errorMessage != null ? errorMessage : "Erreur Twilio");
            } else {
                response.put("status", "success");
                response.put("statusMessage", status.toString());
            }

            response.put("type", isWhatsapp ? "whatsapp" : "sms");
            response.put("destinataire", sms.getDestinataire());
            response.put("expediteur", sms.getExpediteur());
            response.put("texte", sms.getMessageTexte().getContenu());
            response.put("sid", twilioMsg.getSid());
            response.put("dateCreated", twilioMsg.getDateCreated());

            logger.info("Twilio {} SID: {}, status: {}, errorCode: {}, errorMessage: {}",
                    isWhatsapp ? "WhatsApp" : "SMS", twilioMsg.getSid(), status, errorCode, errorMessage);

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi du message via Twilio : ", e);
            response = TwilioErrorHandler.handleException(e);
        }

        return response;
    }
}
