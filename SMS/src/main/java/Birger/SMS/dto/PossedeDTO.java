package Birger.SMS.dto;

public class PossedeDTO {

    private Long idUtilisateur;
    private Long idNumero; // peut rester si tu veux, mais pas obligatoire
    private String valeurNumero; // ➕ AJOUT
    private Long idPlateforme;
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

    public String getValeurNumero() {
        return valeurNumero;
    }

    public void setValeurNumero(String valeurNumero) {
        this.valeurNumero = valeurNumero;
    }
    
    public Long getIdPlateforme() {
        return idPlateforme;
    }

    public void setIdPlateforme(Long idPlateforme) {
        this.idPlateforme = idPlateforme;
    }
}
