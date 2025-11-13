package Birger.SMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import Birger.SMS.exception.PlateformeNotFoundException;
import Birger.SMS.model.Plateforme;
import Birger.SMS.repository.PlateformeRepository;

@Service
public class PlateformeService {

    private final PlateformeRepository repository;

    public PlateformeService(PlateformeRepository repository) {
        this.repository = repository;
    }

    // Créer une plateforme
    public Plateforme createPlateforme(String nom) {
        try {
            Plateforme plateforme = new Plateforme(nom);
            return repository.save(plateforme);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Une plateforme avec ce nom existe déjà !");
        }
    }

    // Lister toutes les plateformes
    public List<Plateforme> getAllPlateformes() {
        return repository.findAll();
    }

    // Trouver par id
    public Optional<Plateforme> getPlateformeById(Long id) {
        return repository.findById(id);
    }

    // Mettre à jour
    public Plateforme updatePlateforme(Long id, String nom) {
        Plateforme plateforme = repository.findById(id)
                .orElseThrow(() -> new PlateformeNotFoundException("Plateforme non trouvée avec l'id " + id));
        plateforme.setNomPlateform(nom);
        try {
            return repository.save(plateforme);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Une plateforme avec ce nom existe déjà !");
        }
    }

    // Supprimer
    public void deletePlateforme(Long id) {
        if (!repository.existsById(id)) {
            throw new PlateformeNotFoundException("Plateforme non trouvée avec l'id " + id);
        }
        repository.deleteById(id);
    }
}
