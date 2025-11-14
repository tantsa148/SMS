package Birger.SMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Birger.SMS.model.DisponibleSur;
import Birger.SMS.model.DisponibleSurId;

public interface DisponibleSurRepository extends JpaRepository<DisponibleSur, DisponibleSurId> {

    List<DisponibleSur> findByNumero_Id(Long idNumero);

    List<DisponibleSur> findByPlateforme_Id(Long idPlateforme);

}
