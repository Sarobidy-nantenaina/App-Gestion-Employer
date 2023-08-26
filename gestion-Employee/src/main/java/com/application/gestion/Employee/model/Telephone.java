package com.application.gestion.Employee.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Telephone {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String phoneNumber;

  @ManyToOne
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @ManyToOne
  @JoinColumn(name = "entreprise_id")
  private Entreprise entreprise;

  // Constructeur avec phoneNumber et employee
  public Telephone(String phoneNumber, Employee employee) {
    this.phoneNumber = phoneNumber;
    this.employee = employee;
  }

  public Telephone(String phoneNumber, Entreprise entreprise) {
    this.phoneNumber = phoneNumber;
    this.entreprise = entreprise;
  }

}