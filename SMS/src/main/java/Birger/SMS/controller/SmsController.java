package Birger.SMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.model.SMS;
import Birger.SMS.service.SmsService;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @PostMapping("/envoyer")
    public String envoyerSms(@RequestBody SMS sms) {
        // Appel du service et récupération du message (succès ou erreur)
        String resultMessage = smsService.envoyerSMS(sms);
        return resultMessage;
    }
}
