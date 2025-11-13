package Birger.SMS.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "possede")
public class Possede {

    @EmbeddedId
    private PossedeId id;

    @ManyToOne
    @MapsId("idUtilisateur")
    @JoinColumn(name = "id_utilisateur")
    private User utilisateur;

    @ManyToOne
    @MapsId("idNumero")
    @JoinColumn(name = "id_numero")
    private Numero numero;

    @Column(name = "date_creation", insertable = false, updatable = false)
    private LocalDateTime dateCreation;

    public Possede() {}

    public Possede(User utilisateur, Numero numero) {
        this.utilisateur = utilisateur;
        this.numero = numero;
        this.id = new PossedeId(utilisateur.getId(), numero.getId());
    }

    public PossedeId getId() {
        return id;
    }

    public User getUtilisateur() {
        return utilisateur;
    }

    public Numero getNumero() {
        return numero;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
}
