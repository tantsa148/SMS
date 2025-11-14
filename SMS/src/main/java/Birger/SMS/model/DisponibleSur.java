package Birger.SMS.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "disponible_sur")
public class DisponibleSur {

    @EmbeddedId
    private DisponibleSurId id;

    @ManyToOne
    @MapsId("idNumero")
    @JoinColumn(name = "id_numero")
    private Numero numero;

    @ManyToOne
    @MapsId("idPlateforme")
    @JoinColumn(name = "id_plateforme")
    private Plateforme plateforme;

    public DisponibleSur() {}

    public DisponibleSur(Numero numero, Plateforme plateforme) {
        this.numero = numero;
        this.plateforme = plateforme;
        this.id = new DisponibleSurId(numero.getId(), plateforme.getId());
    }

    public DisponibleSurId getId() {
        return id;
    }

    public void setId(DisponibleSurId id) {
        this.id = id;
    }

    public Numero getNumero() {
        return numero;
    }

    public void setNumero(Numero numero) {
        this.numero = numero;
    }

    public Plateforme getPlateforme() {
        return plateforme;
    }

    public void setPlateforme(Plateforme plateforme) {
        this.plateforme = plateforme;
    }
}
