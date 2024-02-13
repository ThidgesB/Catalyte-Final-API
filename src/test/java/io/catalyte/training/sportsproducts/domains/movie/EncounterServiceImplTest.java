package io.catalyte.training.sportsproducts.domains.movie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.catalyte.training.sportsproducts.domains.encounters.Encounter;
import io.catalyte.training.sportsproducts.domains.encounters.EncounterRepository;
import io.catalyte.training.sportsproducts.domains.encounters.EncounterServiceImpl;
import io.catalyte.training.sportsproducts.domains.patient.Patient;
import io.catalyte.training.sportsproducts.domains.patient.PatientRepository;
import io.catalyte.training.sportsproducts.exceptions.ResourceNotFound;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

public class EncounterServiceImplTest {

  @InjectMocks
  private EncounterServiceImpl encounterService;

  @Mock
  private EncounterRepository encounterRepository;

  @Mock
  private PatientRepository patientRepository;

  Patient testPatient;

  Encounter testEncounter;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    testPatient = new Patient();
    testPatient.setId(1L);
    testPatient.setFirstName("Bob");
    testPatient.setLastName("Ross");
    testPatient.setSsn("123-45-6789");
    testPatient.setEmail("rob@hob.com");
    testPatient.setStreet("Willow Lane");
    testPatient.setCity("Stardew");
    testPatient.setState("KS");
    testPatient.setPostal("12345");
    testPatient.setAge(30);
    testPatient.setHeight(70);
    testPatient.setWeight(225);
    testPatient.setInsurance("Self Insured");
    testPatient.setGender("Male");
    when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));

    testEncounter = new Encounter();
    testEncounter.setPatient(testPatient);
    testEncounter.setNotes("notes");
    testEncounter.setVisitCode("A3W 3W3");
    testEncounter.setProvider("provider");
    testEncounter.setBillingCode("123.456.789-00");
    testEncounter.setIcd10("Z99");
    testEncounter.setTotalCost(100.99);
    testEncounter.setCopay(10.99);
    testEncounter.setChiefComplaint("Tooth hurts");
    testEncounter.setPulse(20);
    testEncounter.setSystolic(20);
    testEncounter.setDiastolic(20);
    testEncounter.setDate(LocalDate.parse("2022-05-26"));
    when(encounterRepository.save(any(Encounter.class))).thenReturn(testEncounter);
  }

  @Test
  public void getEncountersByPatientId_ReturnsEncounters() {
    Long patientId = 1L;

    List<Encounter> mockEncounters = Arrays.asList(testEncounter);
    when(encounterRepository.findByPatient_Id(patientId)).thenReturn(mockEncounters);

    List<Encounter> returnedEncounters = encounterService.getEncountersByPatientId(patientId);

    assertNotNull(returnedEncounters);
    assertEquals(1, returnedEncounters.size());
    assertEquals(testEncounter, returnedEncounters.get(0));
  }

  @Test
  public void getEncountersByPatientId_NonexistantId_ReturnsEmptyList() {
    Long nonExistentPatientId = 999L;

    when(patientRepository.findById(nonExistentPatientId)).thenReturn(Optional.empty());

    List<Encounter> returnedEncounters = encounterService.getEncountersByPatientId(nonExistentPatientId);

    assertNotNull(returnedEncounters);
    assertTrue(returnedEncounters.isEmpty());
  }

  @Test
  public void createEncounterWithValidData_ReturnsEncounter() {
    Long patientId = 1L;
    Encounter encounter = new Encounter();

    encounter.setPatient(testPatient);
    encounter.setNotes("notes");
    encounter.setVisitCode("N3W 3C3");
    encounter.setProvider("provider");
    encounter.setBillingCode("123.456.789-00");
    encounter.setIcd10("Z99");
    encounter.setTotalCost(100.99);
    encounter.setCopay(10.99);
    encounter.setChiefComplaint("Tooth hurts");
    encounter.setPulse(20);
    encounter.setSystolic(20);
    encounter.setDiastolic(20);
    encounter.setDate(LocalDate.parse("2022-05-26"));

    when(patientRepository.findById(patientId)).thenReturn(Optional.of(new Patient()));
    Encounter returnedEncounter = encounterService.createEncounter(patientId, encounter);

    assertNotNull(returnedEncounter);
  }

  @Test
  public void createEncounter_InvalidData() {
    Long patientId = 1L;
    Encounter encounter = new Encounter();

      when(patientRepository.findById(patientId)).thenReturn(Optional.of(new Patient()));

      assertThrows(ResponseStatusException.class, () -> {
      encounterService.createEncounter(patientId, encounter);
    });
  }

  @Test
  public void validateEncounter_InvalidVisitCode() {
    Encounter encounter = new Encounter();
    testEncounter.setVisitCode("AWS 343");

    assertThrows(ResponseStatusException.class, () -> {
      encounterService.createEncounter(1L, encounter);
    });
  }

  @Test
  public void updateEncounter_ValidInput_ReturnsUpdatedEncounter() {
    Long patientId = 1L;
    Long encounterId = 2L;
    when(encounterRepository.findById(encounterId)).thenReturn(Optional.of(testEncounter));

    Encounter updatedEncounter = encounterService.updateEncounter(patientId, encounterId, testEncounter);

    assertNotNull(updatedEncounter);
    assertEquals(testEncounter, updatedEncounter);
  }


  @Test
  public void updateEncounter_PatientNotFound() {
    Long patientId = 1L;
    Long encounterId = 2L;
    when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

    ResourceNotFound exception = assertThrows(ResourceNotFound.class, () -> {
      encounterService.updateEncounter(patientId, encounterId, testEncounter);
    });

    assertEquals("Patient with ID " + patientId + " not found", exception.getMessage());
  }

  @Test
  public void updateEncounter_EncounterNotFound() {
    Long patientId = 1L;
    Long nonExistentEncounterId = 2L;

    when(encounterRepository.findById(nonExistentEncounterId)).thenReturn(Optional.empty());

    ResourceNotFound exception = assertThrows(ResourceNotFound.class, () -> {
      encounterService.updateEncounter(patientId, nonExistentEncounterId, testEncounter);
    });

    assertEquals("Encounter with ID " + nonExistentEncounterId + " not found", exception.getMessage());
  }
}
