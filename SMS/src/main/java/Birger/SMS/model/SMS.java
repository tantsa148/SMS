package Birger.SMS.model;

import java.time.LocalDateTime;

public class SMS {
    private String expediteur;
    private String destinataire;
    private MessageTexte messageTexte;
    private LocalDateTime dateEnvoi;

    public SMS() {
        this.dateEnvoi = LocalDateTime.now(); // date actuelle
    }

    public SMS(String expediteur, String destinataire, MessageTexte messageTexte) {
        this.expediteur = expediteur;
        this.destinataire = destinataire;
        this.messageTexte = messageTexte;
        this.dateEnvoi = LocalDateTime.now();
    }

    public String getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(String expediteur) {
        this.expediteur = expediteur;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public MessageTexte getMessageTexte() {
        return messageTexte;
    }

    public void setMessageTexte(MessageTexte messageTexte) {
        this.messageTexte = messageTexte;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    @Override
    public String toString() {
        return "SMS{" +
                "expediteur='" + expediteur + '\'' +
                ", destinataire='" + destinataire + '\'' +
                ", messageTexte=" + messageTexte +
                ", dateEnvoi=" + dateEnvoi +
                '}';
    }
}
