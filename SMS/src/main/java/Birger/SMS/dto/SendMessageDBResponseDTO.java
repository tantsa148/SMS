package Birger.SMS.dto;

import java.util.Date;

public class SendMessageDBResponseDTO {

    private String status;
    private String statusMessage;
    private Integer errorCode;
    private String errorMessage;

    private String type; // sms ou whatsapp
    private Long messageId;
    private String destinataire;
    private String expediteur;
    private String texte;

    private String sid;
    private Date dateCreated;

    public SendMessageDBResponseDTO() {
    }

    // Constructor complet
    public SendMessageDBResponseDTO(
            String status,
            String statusMessage,
            Integer errorCode,
            String errorMessage,
            String type,
            Long messageId,
            String destinataire,
            String expediteur,
            String texte,
            String sid,
            Date dateCreated
    ) {
        this.status = status;
        this.statusMessage = statusMessage;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.type = type;
        this.messageId = messageId;
        this.destinataire = destinataire;
        this.expediteur = expediteur;
        this.texte = texte;
        this.sid = sid;
        this.dateCreated = dateCreated;
    }

    // Getters & Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }

    public Integer getErrorCode() { return errorCode; }
    public void setErrorCode(Integer errorCode) { this.errorCode = errorCode; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getMessageId() { return messageId; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }

    public String getDestinataire() { return destinataire; }
    public void setDestinataire(String destinataire) { this.destinataire = destinataire; }

    public String getExpediteur() { return expediteur; }
    public void setExpediteur(String expediteur) { this.expediteur = expediteur; }

    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }

    public String getSid() { return sid; }
    public void setSid(String sid) { this.sid = sid; }

    public Date getDateCreated() { return dateCreated; }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
}
