package com.application.gestion.Employee.repository;

import com.application.gestion.Employee.model.Employee;
import com.application.gestion.Employee.model.Entreprise;
import com.application.gestion.Employee.model.Telephone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelephoneRepository extends JpaRepository<Telephone,Long> {

  void deleteByEmployee(Employee employee);
  void deleteByEntreprise(Entreprise entreprise);

}
