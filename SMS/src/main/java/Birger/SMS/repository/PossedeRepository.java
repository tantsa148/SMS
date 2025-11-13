package Birger.SMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Birger.SMS.model.Possede;
import Birger.SMS.model.PossedeId;

public interface PossedeRepository extends JpaRepository<Possede, PossedeId> {
}
