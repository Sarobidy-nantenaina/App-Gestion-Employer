package com.application.gestion.Employee.repository;

import com.application.gestion.Employee.model.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrepriseRepository extends JpaRepository<Entreprise , Long> {


}
