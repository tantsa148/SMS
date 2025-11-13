package Birger.SMS.dto;

public class PossedeDTO {
    private Long idUtilisateur;
    private Long idNumero;

    public PossedeDTO() {}

    public PossedeDTO(Long idUtilisateur, Long idNumero) {
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
}
