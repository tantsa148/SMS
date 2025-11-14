package Birger.SMS.dto;

public class DisponibleSurDTO {

    private Long idNumero;
    private Long idPlateforme;

    public DisponibleSurDTO() {}

    public DisponibleSurDTO(Long idNumero, Long idPlateforme) {
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
}
