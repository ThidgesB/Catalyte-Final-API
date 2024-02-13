package io.catalyte.training.sportsproducts.domains.encounters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.catalyte.training.sportsproducts.domains.patient.Patient;
import java.time.LocalDate;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Encounter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Transient
  private Long patientId;
  @ManyToOne
  @JoinColumn(name = "patientId")
  private Patient patient;
  private String notes;
  private String visitCode;
  private String provider;
  private String billingCode;
  private String icd10;
  private Double totalCost;
  private Double copay;
  private String chiefComplaint;
  private Integer pulse;
  private Integer systolic;
  private Integer diastolic;
  private LocalDate date;

  public Encounter() {
  }

  public Encounter(Long id, String notes, String visitCode, String provider,
      String billingCode, String icd10, Double totalCost, Double copay, String chiefComplaint,
      Integer pulse, Integer systolic, Integer diastolic, LocalDate date, Patient patient) {
    this.id = id;
    this.notes = notes;
    this.visitCode = visitCode;
    this.provider = provider;
    this.billingCode = billingCode;
    this.icd10 = icd10;
    this.totalCost = totalCost;
    this.copay = copay;
    this.chiefComplaint = chiefComplaint;
    this.pulse = pulse;
    this.systolic = systolic;
    this.diastolic = diastolic;
    this.date = date;
    this.patient = patient;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getVisitCode() {
    return visitCode;
  }

  public void setVisitCode(String visitCode) {
    this.visitCode = visitCode;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getBillingCode() {
    return billingCode;
  }

  public void setBillingCode(String billingCode) {
    this.billingCode = billingCode;
  }

  public String getIcd10() {
    return icd10;
  }

  public void setIcd10(String icd10) {
    this.icd10 = icd10;
  }

  public Double getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(Double totalCost) {
    this.totalCost = totalCost;
  }

  public Double getCopay() {
    return copay;
  }

  public void setCopay(Double copay) {
    this.copay = copay;
  }

  public String getChiefComplaint() {
    return chiefComplaint;
  }

  public void setChiefComplaint(String chiefComplaint) {
    this.chiefComplaint = chiefComplaint;
  }

  public Integer getPulse() {
    return pulse;
  }

  public void setPulse(Integer pulse) {
    this.pulse = pulse;
  }

  public Integer getSystolic() {
    return systolic;
  }

  public void setSystolic(Integer systolic) {
    this.systolic = systolic;
  }

  public Integer getDiastolic() {
    return diastolic;
  }

  public void setDiastolic(Integer diastolic) {
    this.diastolic = diastolic;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  @JsonIgnore
  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public Long getPatientId() {
    if (patient != null) {
      return patient.getId();
    }
    return null;
  }

  public void setPatientId(Long patientId) {
    this.patientId = patientId;
  }
}
