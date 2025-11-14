package Birger.SMS.dto;

import Birger.SMS.model.Numero;
import Birger.SMS.model.Plateforme;

public class DisponibleSurResponseDTO {

    private Long idNumero;
    private String valeurNumero;
    private Long idPlateforme;
    private String nomPlateforme;

    public DisponibleSurResponseDTO(Numero numero, Plateforme plateforme) {
        this.idNumero = numero.getId();
        this.valeurNumero = numero.getValeurNumero();
        this.idPlateforme = plateforme.getId();
        this.nomPlateforme = plateforme.getNomPlateform();
    }

    // ---------- GETTERS ----------
    public Long getIdNumero() {
        return idNumero;
    }

    public String getValeurNumero() {
        return valeurNumero;
    }

    public Long getIdPlateforme() {
        return idPlateforme;
    }

    public String getNomPlateforme() {
        return nomPlateforme;
    }

    // ---------- SETTERS ----------
    public void setIdNumero(Long idNumero) {
        this.idNumero = idNumero;
    }

    public void setValeurNumero(String valeurNumero) {
        this.valeurNumero = valeurNumero;
    }

    public void setIdPlateforme(Long idPlateforme) {
        this.idPlateforme = idPlateforme;
    }

    public void setNomPlateforme(String nomPlateforme) {
        this.nomPlateforme = nomPlateforme;
    }
}
