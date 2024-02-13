package io.catalyte.training.sportsproducts.domains.patient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.catalyte.training.sportsproducts.domains.encounters.Encounter;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Patient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String firstName;
  private String lastName;
  private String ssn;
  private String email;
  private String street;
  private String city;
  private String state;
  private String postal;
  private int age;
  private int height;
  private int weight;
  private String insurance;
  private String gender;
  @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Encounter> encounters;

  public Patient() {
  }

  public Patient(Long id, String firstName, String lastName, String ssn, String email,
      String street,
      String city, String state, String postal, int age, int height, int weight, String insurance,
      String gender, List<Encounter> encounters) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.ssn = ssn;
    this.email = email;
    this.street = street;
    this.city = city;
    this.state = state;
    this.postal = postal;
    this.age = age;
    this.height = height;
    this.weight = weight;
    this.insurance = insurance;
    this.gender = gender;
    this.encounters = encounters;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getSsn() {
    return ssn;
  }

  public void setSsn(String ssn) {
    this.ssn = ssn;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getPostal() {
    return postal;
  }

  public void setPostal(String postal) {
    this.postal = postal;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public String getInsurance() {
    return insurance;
  }

  public void setInsurance(String insurance) {
    this.insurance = insurance;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public List<Encounter> getEncounters() {
    return encounters;
  }
  public void setEncounters(List<Encounter> encounters) {
    this.encounters = encounters;
  }
}

