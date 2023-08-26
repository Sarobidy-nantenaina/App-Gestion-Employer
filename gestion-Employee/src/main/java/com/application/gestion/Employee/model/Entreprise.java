package com.application.gestion.Employee.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Entreprise {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nom;
  private String description;
  private String slogan;
  private String adresse;
  private String emailContact;
  @Column(columnDefinition = "text")
  private String telephones;
  private String nif;
  private String stat;
  private String rcs;
  @Column(columnDefinition = "text")
  private String logo;

}
