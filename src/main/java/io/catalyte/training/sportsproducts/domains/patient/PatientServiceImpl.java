package io.catalyte.training.sportsproducts.domains.patient;

import io.catalyte.training.sportsproducts.domains.encounters.Encounter;
import io.catalyte.training.sportsproducts.domains.encounters.EncounterRepository;
import io.catalyte.training.sportsproducts.exceptions.DatabaseNotAvailableException;
import io.catalyte.training.sportsproducts.exceptions.ResourceNotFound;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service layer for Patient-related operations
 */
@Service
public class PatientServiceImpl implements PatientService{

  @Autowired
  private PatientRepository patientRepository;

  @Autowired
  private EncounterRepository encounterRepository;

  /**
   * Get all patients from the Database
   * @return List of all patients
   */
  @Override
  public List<Patient> getAllPatients() {
    try {
      return patientRepository.findAll();
    } catch (CannotCreateTransactionException e) {
      throw new DatabaseNotAvailableException(e.getMessage());
    }
  }

  /**
   * Get a patient by its unique ID
   * @param id ID of the patient to retrieve
   * @return The patient with the provided ID
   */
  @Override
  public Patient getPatientById(Long id) {
    Patient patient;

    try {
      patient = patientRepository.findById(id).orElse(null);
    } catch (CannotCreateTransactionException e) {
      throw new DatabaseNotAvailableException(e.getMessage());
    }
    if (patient != null) {
      return patient;
    } else {
      throw new ResourceNotFound("Get by ID failed, it does not exist in the database");
    }
  }

  /**
   * Create a new patient in the database
   *
   * @param patient The patient data to save
   * @return The saved patient
   */
  @Override
  public Patient createPatient(Patient patient) {
    List<String> validationErrors = validatePatient(patient);

    Patient existingPatient;
    try {
      existingPatient = patientRepository.findByEmail(patient.getEmail());
    } catch (DataAccessResourceFailureException e) {
      throw new DatabaseNotAvailableException(e.getMessage());
    }

    if(existingPatient != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use.");
    }

    if(!validationErrors.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", validationErrors));
    }

    try {
      return patientRepository.save(patient);
    } catch (DataAccessResourceFailureException e) {
      throw new DatabaseNotAvailableException(e.getMessage());
    }
  }

  /**
   * Edit an existing patient by id
   *
   * @param id The id of the patient to update
   * @param updatedPatient New patient data
   * @return Updated patient
   */
  @Override
  public Patient updatePatient(Long id, Patient updatedPatient) {
    boolean exists;
    try {
      exists = patientRepository.existsById(id);
    } catch (CannotCreateTransactionException e) {
      throw new DatabaseNotAvailableException(e.getMessage());
    }

    if (!exists) {
      throw new ResourceNotFound("Patient with id " + id + " not found.");
    }

    Patient existingPatientWithEmail;
    try {
      existingPatientWithEmail = patientRepository.findByEmail(updatedPatient.getEmail());
    } catch (DataAccessResourceFailureException e) {
      throw new DatabaseNotAvailableException(e.getMessage());
    }

    if (existingPatientWithEmail != null && !existingPatientWithEmail.getId().equals(id)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email exists on another patient.");
    }

    List<String> validationErrors = validatePatient(updatedPatient);
    if (!validationErrors.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", validationErrors));
    }

    updatedPatient.setId(id);
    try {
      return patientRepository.save(updatedPatient);
    } catch (CannotCreateTransactionException e) {
      throw new DatabaseNotAvailableException(e.getMessage());
    }
  }

  /**
   * Delete a patient from the database
   * @param id Id of patient to delete
   */
  @Override
  public void deletePatient(Long id) {
    boolean exists;
    try {
      exists = patientRepository.existsById(id);
    } catch (CannotCreateTransactionException e) {
      throw new DatabaseNotAvailableException(e.getMessage());
    }

    if (!exists) {
      throw new ResourceNotFound("Patient with id " + id + " not found.");
    }

    List<Encounter> encounters;
    try {
      encounters = encounterRepository.findByPatient_Id(id);
    } catch (CannotCreateTransactionException e) {
      throw new DatabaseNotAvailableException(e.getMessage());
    }

    if (!encounters.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Patient has encounter(s) and cannot be deleted.");
    }

    try {
      patientRepository.deleteById(id);
    } catch (DataAccessResourceFailureException e) {
      throw new DatabaseNotAvailableException(e.getMessage());
    }
  }

  /**
   * Validate patient data
   * @param patient Patient data to validate
   * @return List of validation error messages
   */
  private List<String> validatePatient(Patient patient) {
    List<String> validationErrors = new ArrayList<>();

    if (patient.getFirstName() == null || patient.getFirstName().trim().isEmpty()) {
      validationErrors.add("First Name is required.");
    }

    if (patient.getLastName() == null || patient.getLastName().trim().isEmpty()) {
      validationErrors.add("Last Name is required.");
    }

    if (patient.getSsn() == null || !patient.getSsn().matches("^\\d{3}-\\d{2}-\\d{4}$")) {
      validationErrors.add("SSN must be in format DDD-DD-DDDD.");
    }

    if (patient.getEmail() == null || !patient.getEmail().matches("^.+@.+\\..+$")) {
      validationErrors.add("Email format is invalid.");
    }

    if (patient.getStreet() == null) {
      validationErrors.add("Street must be included");
    }

    if (patient.getCity() == null) {
      validationErrors.add("City must be included");
    }

    if (patient.getState() == null || !patient.getState().matches("^[A-Z]{2}$")) {
      validationErrors.add("State must be two uppercase characters.");
    }

    if (patient.getPostal() == null || !patient.getPostal().matches("^\\d{5}$|^\\d{5}-\\d{4}$")) {
      validationErrors.add("Postal must be in format DDDDD or DDDDD-DDDD.");
    }

    if (patient.getAge() <= 0) {
      validationErrors.add("Age must be a positive number.");
    }

    if (patient.getHeight() <= 0) {
      validationErrors.add("Height must be a positive number.");
    }

    if (patient.getWeight() <= 0) {
      validationErrors.add("Weight must be a positive number.");
    }

    if (patient.getGender() == null ||
        (!patient.getGender().equals("Male") &&
            !patient.getGender().equals("Female") &&
            !patient.getGender().equals("Other"))) {
      validationErrors.add("Gender must be 'Male', 'Female', or 'Other'.");
    }

    return validationErrors;
  }

}
