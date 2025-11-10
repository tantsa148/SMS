package Birger.SMS.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import Birger.SMS.model.SMS;

@Service
public class WhatsAppService {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    /**
     * Envoie un message WhatsApp et retourne un message de succès ou d'erreur
     */
    public String envoyerWhatsApp(SMS sms) {
        try {
            // Affiche le contenu complet du SMS/WhatsApp pour debug/log
            System.out.println("Envoi du WhatsApp : " + sms.toString());

            Twilio.init(accountSid, authToken);

            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + sms.getDestinataire()), // destinataire avec préfixe
                    new PhoneNumber("whatsapp:" + sms.getExpediteur()), // sandbox ou numéro business
                    sms.getMessageTexte().getContenu()
            ).create();

            System.out.println("WhatsApp envoyé avec SID : " + message.getSid());
            System.out.println("Détails du message : " + sms);

            return "✅ WhatsApp envoyé : " + message.getSid() + " | " + sms.toString();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du WhatsApp : " + e.getMessage());
            return "❌ Erreur lors de l'envoi du WhatsApp : " + e.getMessage() + " | Message : " + sms.toString();
        }
    }
}
