package Birger.SMS.utils;

import java.util.HashMap;
import java.util.Map;

import com.twilio.exception.ApiException;

public class TwilioErrorHandler {

    /**
     * Gère les erreurs renvoyées par Twilio ou l'application et renvoie une Map prête à retourner.
     */
    public static Map<String, Object> handleException(Exception e) {
        Map<String, Object> response = new HashMap<>();

        if (e instanceof ApiException apiEx) {
            // Erreur spécifique Twilio (numéro invalide, sandbox non connecté, etc.)
            response.put("status", "error");
            response.put("type", "Twilio API Exception");
            response.put("errorCode", apiEx.getCode());
            response.put("message", apiEx.getMessage());
            response.put("moreInfo", apiEx.getMoreInfo());

        } else if (e instanceof IllegalArgumentException) {
            response.put("status", "error");
            response.put("type", "Validation");
            response.put("message", e.getMessage());

        } else if (e instanceof RuntimeException) {
            response.put("status", "error");
            response.put("type", "Application");
            response.put("message", e.getMessage());

        } else {
            response.put("status", "error");
            response.put("type", "Unexpected");
            response.put("message", e.getMessage());
        }

        return response;
    }
}
