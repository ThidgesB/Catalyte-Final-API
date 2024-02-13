package io.catalyte.training.sportsproducts.domains.encounters;

import static io.catalyte.training.sportsproducts.constants.Paths.PATIENTS_PATH;

import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = PATIENTS_PATH)
public class EncounterController {
  Logger logger = LogManager.getLogger(EncounterController.class);

  @Autowired
  private EncounterService encounterService;

  @GetMapping("/{patientId}/encounters")
  public ResponseEntity<List<Encounter>> getEncountersByPatientId(@PathVariable Long patientId) {
    List<Encounter> encounters = encounterService.getEncountersByPatientId(patientId);
    return new ResponseEntity<>(encounters, HttpStatus.OK);
  }

  @PostMapping("/{patientId}/encounters")
  public ResponseEntity<Encounter> createEncounter(@PathVariable Long patientId, @RequestBody Encounter encounter) {
    Encounter createdEncounter = encounterService.createEncounter(patientId, encounter);

    if (createdEncounter != null) {
      return new ResponseEntity<>(createdEncounter, HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/{patientId}/encounters/{encounterId}")
  public ResponseEntity<String> updateEncounter(@PathVariable Long patientId, @PathVariable Long encounterId, @RequestBody Encounter encounter) {
      Encounter updatedEncounter = encounterService.updateEncounter(patientId, encounterId, encounter);
      return new ResponseEntity(updatedEncounter, HttpStatus.OK);
  }

}
