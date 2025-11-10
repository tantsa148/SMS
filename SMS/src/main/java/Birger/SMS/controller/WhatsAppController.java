package Birger.SMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.model.SMS;
import Birger.SMS.service.WhatsAppService;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    @Autowired
    private WhatsAppService whatsAppService;

    @PostMapping("/envoyer")
    public String envoyerWhatsApp(@RequestBody SMS sms) {
        // Appel du service WhatsApp et récupération du message (succès ou erreur)
        String resultMessage = whatsAppService.envoyerWhatsApp(sms);
        return resultMessage;
    }
}
