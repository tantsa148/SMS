package Birger.SMS.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import Birger.SMS.dto.DisponibleSurDTO;
import Birger.SMS.dto.DisponibleSurResponseDTO;
import Birger.SMS.model.DisponibleSur;
import Birger.SMS.model.DisponibleSurId;
import Birger.SMS.model.Numero;
import Birger.SMS.model.Plateforme;
import Birger.SMS.repository.DisponibleSurRepository;
import Birger.SMS.repository.NumeroRepository;
import Birger.SMS.repository.PlateformeRepository;

@Service
public class DisponibleSurService {

    private final DisponibleSurRepository disponibleSurRepository;
    private final NumeroRepository numeroRepository;
    private final PlateformeRepository plateformeRepository;

    public DisponibleSurService(DisponibleSurRepository repo,
                                NumeroRepository numRepo,
                                PlateformeRepository platRepo) {
        this.disponibleSurRepository = repo;
        this.numeroRepository = numRepo;
        this.plateformeRepository = platRepo;
    }

    // GET ALL
    public List<DisponibleSurResponseDTO> getAll() {
        return disponibleSurRepository.findAll().stream()
                .map(d -> new DisponibleSurResponseDTO(d.getNumero(), d.getPlateforme()))
                .collect(Collectors.toList());
    }

    // CREATE
    public DisponibleSurResponseDTO create(DisponibleSurDTO dto) {

        Numero numero = numeroRepository.findById(dto.getIdNumero())
                .orElseThrow(() -> new RuntimeException("Numéro introuvable"));

        Plateforme plateforme = plateformeRepository.findById(dto.getIdPlateforme())
                .orElseThrow(() -> new RuntimeException("Plateforme introuvable"));

        DisponibleSurId id = new DisponibleSurId(numero.getId(), plateforme.getId());

        if (disponibleSurRepository.existsById(id)) {
            throw new RuntimeException("Cette association existe déjà !");
        }

        DisponibleSur newAssoc = new DisponibleSur(numero, plateforme);
        DisponibleSur saved = disponibleSurRepository.save(newAssoc);

        return new DisponibleSurResponseDTO(saved.getNumero(), saved.getPlateforme());
    }

    // DELETE
    public void delete(Long idNumero, Long idPlateforme) {
        DisponibleSurId id = new DisponibleSurId(idNumero, idPlateforme);

        if (!disponibleSurRepository.existsById(id)) {
            throw new RuntimeException("Association non trouvée !");
        }

        disponibleSurRepository.deleteById(id);
    }
}
