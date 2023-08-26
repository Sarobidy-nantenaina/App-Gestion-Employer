package com.application.gestion.Employee.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String matricule;

    private String name;
    private String firstname;
    private LocalDate birthdate;
    @Column(columnDefinition = "text")
    private String photo;
    private String sexe; // H/F
    @Column(columnDefinition = "text")
    private String telephones;
    private String adresse;
    private String emailPerso;
    private String emailPro;
    private String cinNumero;
    private LocalDate cinDateDelivrance;
    private String cinLieuDelivrance;
    private String fonction;
    private int nombreEnfants;
    private LocalDate dateEmbauche;
    private LocalDate dateDepart;
    private String categorieSocioPro;
    private String numeroCnaps;
    private BigDecimal salaireBrut;
    public Employee() {
        this.matricule = null;
    }

}

