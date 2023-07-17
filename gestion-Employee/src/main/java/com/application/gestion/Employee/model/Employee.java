package com.application.gestion.Employee.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

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
    public Employee() {
        this.matricule = generateMatricule();
    }

    private String generateMatricule() {
        if (matricule != null) {
            return matricule; // Retourner le matricule existant si disponible
        }
        long mostSignificantBits = UUID.randomUUID().getMostSignificantBits();
        String matricule = String.valueOf(Math.abs(mostSignificantBits));
        return "STD " + matricule.substring(0, 4);
    }

}

