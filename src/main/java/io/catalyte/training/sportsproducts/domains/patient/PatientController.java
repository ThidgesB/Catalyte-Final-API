package io.catalyte.training.sportsproducts.domains.patient;

import static io.catalyte.training.sportsproducts.constants.Paths.PATIENTS_PATH;

import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = PATIENTS_PATH)
public class PatientController {
  Logger logger = LogManager.getLogger(PatientController.class);

  @Autowired
  private PatientService patientService;

  @GetMapping
  public ResponseEntity<List<Patient>> getAllPatients() {
    logger.info("Request received for getAllPatients");
    return new ResponseEntity(patientService.getAllPatients(), HttpStatus.OK);
  }

  @GetMapping(value = "/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
    logger.info("Request received for getPatientById:" + id);

    return new ResponseEntity<>(patientService.getPatientById(id), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
    Patient savedPatient = patientService.createPatient(patient);
    return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
    logger.info("Request received for updatePatient with ID:" + id);
    Patient updatedPatient = patientService.updatePatient(id, patient);
    return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
    patientService.deletePatient(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
