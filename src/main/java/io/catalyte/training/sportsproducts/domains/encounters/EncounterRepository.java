package io.catalyte.training.sportsproducts.domains.encounters;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncounterRepository extends JpaRepository<Encounter, Long>{
  List<Encounter> findByPatient_Id(Long patientId);
}
