package Birger.SMS.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import Birger.SMS.model.Numero;
import Birger.SMS.repository.NumeroRepository;

@Service
public class NumeroService {

    private static final Logger logger = LoggerFactory.getLogger(NumeroService.class);

    private final NumeroRepository numeroRepository;

    public NumeroService(NumeroRepository numeroRepository) {
        this.numeroRepository = numeroRepository;
    }

    public List<Numero> getAllNumeros() {
        logger.info("Récupération de tous les numéros");
        return numeroRepository.findAll();
    }

    public Optional<Numero> getNumeroById(Long id) {
        logger.info("Recherche du numéro avec ID : {}", id);
        return numeroRepository.findById(id);
    }

    public Numero createNumero(Numero numero) {
    logger.info("Création d’un nouveau numéro : {}", numero.getValeurNumero());

    // Vérification valeur vide ou null
    if (numero.getValeurNumero() == null || numero.getValeurNumero().trim().isEmpty()) {
        logger.warn("Tentative de création d'un numéro vide");
        throw new IllegalArgumentException("Le champ 'valeur_numero' ne peut pas être vide");
    }

    if (numeroRepository.existsByValeurNumero(numero.getValeurNumero())) {
        logger.warn("Le numéro {} existe déjà !", numero.getValeurNumero());
        throw new DataIntegrityViolationException("Le numéro existe déjà !");
    }

    Numero saved = numeroRepository.save(numero);
    logger.info("Numéro créé avec succès : {}", saved.getId());
    return saved;
}


    public Numero updateNumero(Long id, Numero updatedNumero) {
        logger.info("Mise à jour du numéro ID : {}", id);
        return numeroRepository.findById(id)
                .map(existing -> {
                    existing.setValeurNumero(updatedNumero.getValeurNumero());
                    Numero saved = numeroRepository.save(existing);
                    logger.info("Numéro ID {} mis à jour avec succès", id);
                    return saved;
                })
                .orElseThrow(() -> new RuntimeException("Numéro non trouvé"));
    }

    public void deleteNumero(Long id) {
        logger.info("Suppression du numéro ID : {}", id);
        numeroRepository.deleteById(id);
        logger.info("Numéro ID {} supprimé", id);
    }
}
