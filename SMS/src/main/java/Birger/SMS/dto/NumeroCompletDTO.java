package Birger.SMS.dto;

import java.util.List;

public class NumeroCompletDTO {
    private Long id;
    private String valeurNumero;
    private String proprietaire;
    private List<String> plateformes;

    // Constructeurs
    public NumeroCompletDTO() {}

    public NumeroCompletDTO(Long id, String valeurNumero, String proprietaire, List<String> plateformes) {
        this.id = id;
        this.valeurNumero = valeurNumero;
        this.proprietaire = proprietaire;
        this.plateformes = plateformes;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getValeurNumero() { return valeurNumero; }
    public void setValeurNumero(String valeurNumero) { this.valeurNumero = valeurNumero; }

    public String getProprietaire() { return proprietaire; }
    public void setProprietaire(String proprietaire) { this.proprietaire = proprietaire; }

    public List<String> getPlateformes() { return plateformes; }
    public void setPlateformes(List<String> plateformes) { this.plateformes = plateformes; }
}