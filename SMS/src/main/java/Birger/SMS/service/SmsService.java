package Birger.SMS.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import Birger.SMS.model.SMS;
@Service
public class SmsService {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    /**
     * Envoie un SMS et retourne un message de succès ou d'erreur
     */
    public String envoyerSMS(SMS sms) {
        try {
            // Affiche le contenu complet du SMS pour debug/log
            System.out.println("Envoi du SMS : " + sms.toString());

            Twilio.init(accountSid, authToken);

            Message message = Message.creator(
                    new PhoneNumber(sms.getDestinataire()),
                    new PhoneNumber(sms.getExpediteur()),
                    sms.getMessageTexte().getContenu()
            ).create();

            // Affiche le SID et le SMS
            System.out.println("SMS envoyé avec SID : " + message.getSid());
            System.out.println("Détails du SMS : " + sms);

            return "✅ SMS envoyé : " + message.getSid() + " | " + sms.toString();
        } catch (Exception e) {
            // Retourne le message d'erreur pour le contrôleur
            System.err.println("Erreur lors de l'envoi du SMS : " + e.getMessage());
            return "❌ Erreur lors de l'envoi du SMS : " + e.getMessage() + " | SMS : " + sms.toString();
        }
    }
    
}
