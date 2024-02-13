package io.catalyte.training.sportsproducts.domains.encounters;

import java.util.List;

public interface EncounterService {
  Encounter createEncounter(Long patientId, Encounter encounter);
  Encounter updateEncounter(Long patientId, Long encounterId, Encounter encounter);
  List<Encounter> getEncountersByPatientId(Long patientId);

}
