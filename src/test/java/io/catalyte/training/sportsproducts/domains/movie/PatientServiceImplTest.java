package io.catalyte.training.sportsproducts.domains.movie;

import io.catalyte.training.sportsproducts.domains.encounters.Encounter;
import io.catalyte.training.sportsproducts.domains.encounters.EncounterRepository;
import io.catalyte.training.sportsproducts.domains.patient.Patient;
import io.catalyte.training.sportsproducts.domains.patient.PatientRepository;
import io.catalyte.training.sportsproducts.domains.patient.PatientServiceImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import io.catalyte.training.sportsproducts.exceptions.DatabaseNotAvailableException;
import io.catalyte.training.sportsproducts.exceptions.ResourceNotFound;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.server.ResponseStatusException;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(PatientServiceImpl.class)
public class PatientServiceImplTest {

  @InjectMocks
  private PatientServiceImpl patientServiceImpl;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private PatientRepository patientRepository;

  @Mock
  private EncounterRepository encounterRepository;

  Patient testPatient;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    testPatient = new Patient();
    testPatient.setId(1L);
    testPatient.setFirstName("Test Fname");
    testPatient.setLastName("Test Lname");
    testPatient.setSsn("123-45-6789");
    testPatient.setEmail("rob@hob.com");
    testPatient.setStreet("123 Dogland St.");
    testPatient.setCity("Dogtown");
    testPatient.setState("CA");
    testPatient.setPostal("66441");
    testPatient.setAge(30);
    testPatient.setHeight(6);
    testPatient.setWeight(220);
    testPatient.setInsurance("Dog Medical");
    testPatient.setGender("Male");
    when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
  }
  @Test
  public void getAllPatients_ReturnsListOfPatients() {
    List<Patient> patients = new ArrayList<>();
    patients.add(testPatient);
    when(patientRepository.findAll()).thenReturn(patients);

    List<Patient> result = patientServiceImpl.getAllPatients();
    assertEquals(patients, result);
  }

  @Test
  public void getAllPatients_DatabaseException() {
    when(patientRepository.findAll()).thenThrow(CannotCreateTransactionException.class);

    assertThrows(DatabaseNotAvailableException.class, () -> patientServiceImpl.getAllPatients());

    verify(patientRepository).findAll();
  }

  @Test
  public void getPatientById_ReturnsPatient() {
    Patient actual = patientServiceImpl.getPatientById(1L);
    assertEquals(testPatient, actual);
  }

  @Test
  public void getPatientById_ThrowsResourceNotFound() {
    when(patientRepository.findById(any())).thenReturn(Optional.empty());
    assertThrows(ResourceNotFound.class, () -> patientServiceImpl.getPatientById(2L));
  }

  @Test
  public void getPatientById_DatabaseException() {
    when(patientRepository.findById(1L)).thenThrow(CannotCreateTransactionException.class);

    assertThrows(DatabaseNotAvailableException.class, () -> patientServiceImpl.getPatientById(1L));
  }

  @Test
  public void createPatient_ReturnsPatient() {
    when(patientRepository.save(any())).thenReturn(testPatient);
    Patient result = patientServiceImpl.createPatient(testPatient);
    assertEquals(testPatient, result);
  }

  @Test
  public void createPatient_EmailExists() {
    when(patientRepository.findByEmail("rob@hob.com")).thenReturn(testPatient);

    assertThrows(ResponseStatusException.class, () -> patientServiceImpl.createPatient(testPatient));
  }

  @Test
  public void createPatient_ValidationErrors() {
    testPatient.setId(1L);
    testPatient.setFirstName("Xu");
    testPatient.setLastName("Lu");
    testPatient.setSsn("123456789");
    testPatient.setEmail("rob.com");
    testPatient.setStreet("");
    testPatient.setCity("");
    testPatient.setState("California");
    testPatient.setPostal("7");
    testPatient.setAge(-10);
    testPatient.setHeight(-15);
    testPatient.setWeight(-220);
    testPatient.setInsurance("");
    testPatient.setGender("Alien");

    assertThrows(ResponseStatusException.class, () -> patientServiceImpl.createPatient(testPatient));
  }

  @Test
  public void createPatient_DatabaseExceptionDuringSave() {
    when(patientRepository.save(any())).thenThrow(DataAccessResourceFailureException.class);
    assertThrows(DatabaseNotAvailableException.class, () -> patientServiceImpl.createPatient(testPatient));
  }

  @Test
  public void updatePatient_SameEmail_ReturnsUpdatedPatient() {
    when(patientRepository.existsById(1L)).thenReturn(true);
    when(patientRepository.findByEmail("rob@hob.com")).thenReturn(null);
    when(patientRepository.save(testPatient)).thenReturn(testPatient);

    assertNotNull(patientServiceImpl.updatePatient(1L, testPatient));
  }

  @Test
  public void updatePatient_NewEmailAlreadyUsed() {
    Patient anotherPatient = new Patient();
    anotherPatient.setId(2L);
    when(patientRepository.existsById(1L)).thenReturn(true);
    when(patientRepository.findByEmail("newemail@hob.com")).thenReturn(anotherPatient);
    testPatient.setEmail("newemail@hob.com");
    assertThrows(ResponseStatusException.class, () -> patientServiceImpl.updatePatient(1L, testPatient));
  }

  @Test
  public void updatePatient_PatientById_DoesNotExist() {
    when(patientRepository.existsById(1L)).thenReturn(false);

    assertThrows(ResourceNotFound.class, () -> patientServiceImpl.updatePatient(1L, testPatient));
  }

  @Test
  public void updatePatient_DatabaseExceptionWhenCheckingEmail() {
    when(patientRepository.existsById(1L)).thenReturn(true);
    when(patientRepository.findByEmail("rob@hob.com")).thenThrow(DataAccessResourceFailureException.class);
    assertThrows(DatabaseNotAvailableException.class, () -> patientServiceImpl.updatePatient(1L, testPatient));
  }

  @Test
  public void deletePatient_SuccessfulDeletion() {
    when(patientRepository.existsById(1L)).thenReturn(true);
    when(encounterRepository.findByPatient_Id(1L)).thenReturn(new ArrayList<>());

    patientServiceImpl.deletePatient(1L);
  }

  @Test
  public void deletePatient_PatientDoesNotExist() {
    when(patientRepository.existsById(1L)).thenReturn(false);

    assertThrows(ResourceNotFound.class, () -> patientServiceImpl.deletePatient(1L));
  }

  @Test
  public void deletePatient_PatientHasEncounters() {
    when(patientRepository.existsById(1L)).thenReturn(true);
    when(encounterRepository.findByPatient_Id(1L)).thenReturn(Arrays.asList(mock(Encounter.class)));

    assertThrows(ResponseStatusException.class, () -> patientServiceImpl.deletePatient(1L));
  }
}
