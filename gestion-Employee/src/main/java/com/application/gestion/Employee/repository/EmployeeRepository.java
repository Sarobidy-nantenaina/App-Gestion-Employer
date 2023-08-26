package com.application.gestion.Employee.repository;

import com.application.gestion.Employee.model.Employee;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

  boolean existsByMatricule(String matricule);

  // Filtrer par nom
  @Query(value = "SELECT * FROM employee WHERE LOWER(name) LIKE %:name%", nativeQuery = true)
  List<Employee> filterEmployeesByName(String name);

  @Query(value = "SELECT * FROM employee WHERE LOWER(name) LIKE %:name% ORDER BY name ASC", nativeQuery = true)
  List<Employee> filterEmployeesByNameAsc(String name);

  @Query(value = "SELECT * FROM employee WHERE LOWER(name) LIKE %:name% ORDER BY name DESC", nativeQuery = true)
  List<Employee> filterEmployeesByNameDesc(String name);

  // Filtrer par prénom
  @Query(value = "SELECT * FROM employee WHERE LOWER(firstname) LIKE %:firstname%", nativeQuery = true)
  List<Employee> filterEmployeesByFirstname(String firstname);

  @Query(value = "SELECT * FROM employee WHERE LOWER(firstname) LIKE %:firstname% ORDER BY firstname ASC", nativeQuery = true)
  List<Employee> filterEmployeesByFirstnameAsc(String firstname);

  @Query(value = "SELECT * FROM employee WHERE LOWER(firstname) LIKE %:firstname% ORDER BY firstname DESC", nativeQuery = true)
  List<Employee> filterEmployeesByFirstnameDesc(String firstname);

  // Filtrer par sexe
  @Query(value = "SELECT * FROM employee WHERE LOWER(sexe) LIKE %:sexe%", nativeQuery = true)
  List<Employee> filterEmployeesBySexe(String sexe);

  // Filtrer par fonction
  @Query(value = "SELECT * FROM employee WHERE LOWER(fonction) LIKE %:fonction%", nativeQuery = true)
  List<Employee> filterEmployeesByFonction(String fonction);

  @Query(value = "SELECT * FROM employee WHERE LOWER(fonction) LIKE %:fonction% ORDER BY fonction ASC", nativeQuery = true)
  List<Employee> filterEmployeesByFonctionAsc(String fonction);

  @Query(value = "SELECT * FROM employee WHERE LOWER(fonction) LIKE %:fonction% ORDER BY fonction DESC", nativeQuery = true)
  List<Employee> filterEmployeesByFonctionDesc(String fonction);

  // Filtrer par date d'embauche
  @Query(value = "SELECT * FROM employee WHERE date_embauche = :dateEmbauche", nativeQuery = true)
  List<Employee> filterEmployeesByDateEmbauche(LocalDate dateEmbauche);

  @Query(value = "SELECT * FROM employee WHERE date_embauche = :dateEmbauche ORDER BY date_embauche ASC", nativeQuery = true)
  List<Employee> filterEmployeesByDateEmbaucheAsc(LocalDate dateEmbauche);

  @Query(value = "SELECT * FROM employee WHERE date_embauche = :dateEmbauche ORDER BY date_embauche DESC", nativeQuery = true)
  List<Employee> filterEmployeesByDateEmbaucheDesc(LocalDate dateEmbauche);


  // Filtrer par date de départ
  @Query(value = "SELECT * FROM employee WHERE date_depart = :dateDepart", nativeQuery = true)
  List<Employee> filterEmployeesByDateDepart(LocalDate dateDepart);

  @Query(value = "SELECT * FROM employee WHERE date_depart = :dateDepart ORDER BY date_depart ASC", nativeQuery = true)
  List<Employee> filterEmployeesByDateDepartAsc(LocalDate dateDepart);

  @Query(value = "SELECT * FROM employee WHERE date_depart = :dateDepart ORDER BY date_depart DESC", nativeQuery = true)
  List<Employee> filterEmployeesByDateDepartDesc(LocalDate dateDepart);

  // Filtrer par téléphone
  @Query(value = "SELECT * FROM employee WHERE telephones LIKE %:telephone%", nativeQuery = true)
  List<Employee> filterEmployeesByTelephones(String telephone);

  @Query(value = "SELECT * FROM employee WHERE telephones LIKE %:telephone% ORDER BY telephones ASC", nativeQuery = true)
  List<Employee> filterEmployeesByTelephonesAsc(String telephone);

  @Query(value = "SELECT * FROM employee WHERE telephones LIKE %:telephone% ORDER BY telephones DESC", nativeQuery = true)
  List<Employee> filterEmployeesByTelephonesDesc(String telephone);


}

