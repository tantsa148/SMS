package Birger.SMS.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "plateforme")
public class Plateforme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // correspond à SERIAL
    private Long id;

    @Column(name = "nom_plateform", nullable = false, length = 50)
    private String nomPlateform;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    // Constructeurs
    public Plateforme() {}

    public Plateforme(String nomPlateform) {
        this.nomPlateform = nomPlateform;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomPlateform() { return nomPlateform; }
    public void setNomPlateform(String nomPlateform) { this.nomPlateform = nomPlateform; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
