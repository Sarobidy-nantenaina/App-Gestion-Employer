package com.application.gestion.Employee.controler;

import com.application.gestion.Employee.model.Employee;
import com.application.gestion.Employee.model.Telephone;
import com.application.gestion.Employee.service.EmployeeService;
import com.application.gestion.Employee.service.TelephoneService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telephones")
@AllArgsConstructor
public class TelephoneControler {

  private final TelephoneService telephoneService;
  private final EmployeeService employeeService;

  @GetMapping
  public ResponseEntity<List<Telephone>> getAllTelephones() {
    List<Telephone> telephones = telephoneService.getAllTelephones();
    return ResponseEntity.ok(telephones);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Telephone> getTelephoneById(@PathVariable Long id) {
    Telephone telephone = telephoneService.getTelephoneById(id);
    if (telephone != null) {
      return ResponseEntity.ok(telephone);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<Telephone> createTelephone(@RequestBody Telephone telephone) {
    // Vérifier si l'employé associé au numéro de téléphone existe
    Employee employee = employeeService.getEmployeeById(telephone.getEmployee().getId());
    if (employee == null) {
      return ResponseEntity.badRequest().build(); // Renvoyer une erreur 400 si l'employé n'existe pas
    }

    // Associer l'employé au numéro de téléphone
    telephone.setEmployee(employee);

    Telephone savedTelephone = telephoneService.saveTelephone(telephone);
    return ResponseEntity.ok(savedTelephone);
  }


  @PutMapping("/{id}")
  public ResponseEntity<Telephone> updateTelephone(@PathVariable Long id, @RequestBody Telephone telephone) {
    Telephone existingTelephone = telephoneService.getTelephoneById(id);
    if (existingTelephone != null) {
      telephone.setId(id);
      Telephone updatedTelephone = telephoneService.saveTelephone(telephone);
      return ResponseEntity.ok(updatedTelephone);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTelephone(@PathVariable Long id) {
    Telephone existingTelephone = telephoneService.getTelephoneById(id);
    if (existingTelephone != null) {
      telephoneService.deleteTelephone(id);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

}
