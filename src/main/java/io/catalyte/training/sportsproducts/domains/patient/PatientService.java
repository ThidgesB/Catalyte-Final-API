package io.catalyte.training.sportsproducts.domains.patient;

public interface PatientService {
  Object getAllPatients();
  Patient getPatientById(Long id);
  Patient createPatient(Patient patient);
  Patient updatePatient(Long id, Patient patient);
  void deletePatient(Long id);
}
