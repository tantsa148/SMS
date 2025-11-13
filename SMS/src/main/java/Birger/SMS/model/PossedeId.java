package Birger.SMS.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class PossedeId implements Serializable {

    private Long idUtilisateur;
    private Long idNumero;

    public PossedeId() {}

    public PossedeId(Long idUtilisateur, Long idNumero) {
        this.idUtilisateur = idUtilisateur;
        this.idNumero = idNumero;
    }

    public Long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Long getIdNumero() {
        return idNumero;
    }

    public void setIdNumero(Long idNumero) {
        this.idNumero = idNumero;
    }

    @Override
    public int hashCode() {
        return idUtilisateur.hashCode() + idNumero.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PossedeId other = (PossedeId) obj;
        return idUtilisateur.equals(other.idUtilisateur) && idNumero.equals(other.idNumero);
    }
}
