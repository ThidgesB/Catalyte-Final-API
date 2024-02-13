package io.catalyte.training.sportsproducts.domains.encounters;

import io.catalyte.training.sportsproducts.domains.patient.Patient;
import io.catalyte.training.sportsproducts.domains.patient.PatientRepository;
import io.catalyte.training.sportsproducts.exceptions.DatabaseNotAvailableException;
import io.catalyte.training.sportsproducts.exceptions.ResourceNotFound;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service layer for Encounter-related operations
 */
@Service
public class EncounterServiceImpl implements EncounterService {

  @Autowired
  private EncounterRepository encounterRepository;
  @Autowired
  private PatientRepository patientRepository;

  /**
   * Retrieves encounters based on patient id
   *
   * @param patientId Id of the patient for retrieving encounters
   * @return List of encounters
   */
  @Override
  public List<Encounter> getEncountersByPatientId(Long patientId) {
    return encounterRepository.findByPatient_Id(patientId);
  }

  /**
   * Creates a new encounter for a given patient
   *
   * @param patientId id of patient for encounter created
   * @param encounter Encounter details
   * @return Created encounter
   * @throws Exception if database is not available
   */
  @Override
  public Encounter createEncounter(Long patientId, Encounter encounter) {
    Optional<Patient> patientOptional;

    try {
      patientOptional = patientRepository.findById(patientId);
    } catch (CannotCreateTransactionException e) {
      throw new DatabaseNotAvailableException(e.getMessage());
    }

    if (!patientOptional.isPresent()) {
      throw new ResourceNotFound("Patient with ID " + patientId + " not found");
    }

    Patient patient = patientOptional.get();
    encounter.setPatient(patient);

    List<String> validationErrors = validateEncounter(encounter);

    if (!validationErrors.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", validationErrors));
    }

    try {
      return encounterRepository.save(encounter);
    } catch (CannotCreateTransactionException e) {
      throw new DatabaseNotAvailableException(e.getMessage());
    }
  }


  /**
   * Updates details on an existing encounter
   *
   * @param patientId ID of patient associated with encounter
   * @param encounterId Id of the encounter to be edited
   * @param updatedEncounter Updated encounter details
   * @return The updated encounter
   */
  @Override
  public Encounter updateEncounter(Long patientId, Long encounterId, Encounter updatedEncounter) {
    Optional<Patient> patientOptional;
    Optional<Encounter> existingEncounterOptional;

    try {
      patientOptional = patientRepository.findById(patientId);
    } catch (CannotCreateTransactionException e) {
      throw new DatabaseNotAvailableException(e.getMessage());
    }

    if (!patientOptional.isPresent()) {
      throw new ResourceNotFound("Patient with ID " + patientId + " not found");
    }

    try {
      existingEncounterOptional = encounterRepository.findById(encounterId);
    } catch (CannotCreateTransactionException e) {
      throw new DatabaseNotAvailableException(e.getMessage());
    }

    if (!existingEncounterOptional.isPresent()) {
      throw new ResourceNotFound("Encounter with ID " + encounterId + " not found");
    }

    List<String> validationErrors = validateEncounter(updatedEncounter);

    if (!validationErrors.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", validationErrors));
    }

    updatedEncounter.setId(encounterId);
    updatedEncounter.setPatient(patientOptional.get());

    try {
      return encounterRepository.save(updatedEncounter);
    } catch (CannotCreateTransactionException e) {
      throw new DatabaseNotAvailableException(e.getMessage());
    }
  }

  /**
   * Validates given Encounter details
   *
   * @param encounter Encounter to be validated
   * @return List of validation error messages.
   */
  private List<String> validateEncounter(Encounter encounter) {
    List<String> validationErrors = new ArrayList<>();

    if(encounter.getVisitCode() == null || !encounter.getVisitCode().matches("^[A-Za-z]\\d[A-Za-z] \\d[A-Za-z]\\d$")) {
      validationErrors.add("Visit code format is invalid.");
    }

    if(encounter.getProvider() == null || encounter.getProvider().trim().isEmpty()) {
      validationErrors.add("Provider is required.");
    }

    if(encounter.getBillingCode() == null || !encounter.getBillingCode().matches("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$")) {
      validationErrors.add("Billing code format is invalid.");
    }

    if(encounter.getIcd10() == null || !encounter.getIcd10().matches("^[A-Za-z]\\d{2}$")) {
      validationErrors.add("ICD10 code format is invalid.");
    }

    if(encounter.getTotalCost() == null || encounter.getTotalCost() < 0) {
      validationErrors.add("Total cost must be a positive number.");
    }

    if(encounter.getCopay() == null || encounter.getCopay() < 0) {
      validationErrors.add("Copay must be a positive number.");
    }

    if(encounter.getChiefComplaint() == null || encounter.getChiefComplaint().trim().isEmpty()) {
      validationErrors.add("Chief complaint is required.");
    }

    if(encounter.getPulse() != null) {
      if(encounter.getPulse() <= 0) {
        validationErrors.add("Pulse must be a positive number.");
      } else if (!encounter.getPulse().toString().matches("^\\d+$")) {
        validationErrors.add("Pulse value format is invalid.");
      }
    }

    if(encounter.getSystolic() != null) {
      if(!encounter.getSystolic().toString().matches("^\\d+$")) {
        validationErrors.add("Systolic value format is invalid.");
      }
    }

    if(encounter.getDiastolic() != null) {
      if(!encounter.getDiastolic().toString().matches("^\\d+$")) {
        validationErrors.add("Diastolic value format is invalid.");
      }
    }

    if(!isValidDate(encounter.getDate())) {
      validationErrors.add("Date is required and must be in format YYYY-MM-DD");
    }

    return validationErrors;
  }

  /**
   * Checks given date for validation
   *
   * @param date Date to be checked
   * @return true if date is valid; false otherwise
   */
  private boolean isValidDate(LocalDate date) {
    return date != null && !date.isAfter(LocalDate.now());
  }

}
