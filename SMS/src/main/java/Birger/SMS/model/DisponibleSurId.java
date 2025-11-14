package Birger.SMS.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DisponibleSurId implements Serializable {

    @Column(name = "id_numero")
    private Long idNumero;

    @Column(name = "id_plateforme")
    private Long idPlateforme;

    public DisponibleSurId() {}

    public DisponibleSurId(Long idNumero, Long idPlateforme) {
        this.idNumero = idNumero;
        this.idPlateforme = idPlateforme;
    }

    public Long getIdNumero() {
        return idNumero;
    }

    public void setIdNumero(Long idNumero) {
        this.idNumero = idNumero;
    }

    public Long getIdPlateforme() {
        return idPlateforme;
    }

    public void setIdPlateforme(Long idPlateforme) {
        this.idPlateforme = idPlateforme;
    }

    @Override
    public int hashCode() {
        return idNumero.hashCode() + idPlateforme.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        DisponibleSurId other = (DisponibleSurId) obj;

        return idNumero.equals(other.idNumero) &&
               idPlateforme.equals(other.idPlateforme);
    }
}
