package Birger.SMS.dto;

import java.time.LocalDateTime;

import Birger.SMS.model.Numero;
import Birger.SMS.model.User;

public class PossedeResponseDTO {

    private Long idUtilisateur;
    private String username;
    private Long idNumero;
    private String valeurNumero;
    private LocalDateTime dateCreation;

    // Constructeur à partir de l'utilisateur et du numéro
    public PossedeResponseDTO(User user, Numero numero) {
        this.idUtilisateur = user.getId();
        this.username = user.getUsername();
        this.idNumero = numero.getId();
        this.valeurNumero = numero.getValeurNumero();
        this.dateCreation = numero.getDateCreation(); // si tu veux la date du numéro
    }

    // Getters et setters
    public Long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getIdNumero() {
        return idNumero;
    }

    public void setIdNumero(Long idNumero) {
        this.idNumero = idNumero;
    }

    public String getValeurNumero() {
        return valeurNumero;
    }

    public void setValeurNumero(String valeurNumero) {
        this.valeurNumero = valeurNumero;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
}
