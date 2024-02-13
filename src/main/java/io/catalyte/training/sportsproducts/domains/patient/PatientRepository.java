package io.catalyte.training.sportsproducts.domains.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

  Patient findByEmail(String email);
}
