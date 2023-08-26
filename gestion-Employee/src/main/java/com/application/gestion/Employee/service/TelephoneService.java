package com.application.gestion.Employee.service;

import com.application.gestion.Employee.model.Employee;
import com.application.gestion.Employee.model.Entreprise;
import com.application.gestion.Employee.model.Telephone;
import com.application.gestion.Employee.repository.TelephoneRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TelephoneService {

  private final TelephoneRepository telephoneRepository;

  public List<Telephone> getAllTelephones() {
    return telephoneRepository.findAll();
  }

  public Telephone getTelephoneById(Long id) {
    return telephoneRepository.findById(id).orElse(null);
  }

  public Telephone saveTelephone(Telephone telephone) {
    return telephoneRepository.save(telephone);
  }

  public void deleteTelephone(Long id) {
    telephoneRepository.deleteById(id);
  }
  @Transactional
  public void deleteByEmployee(Employee employee) {
    telephoneRepository.deleteByEmployee(employee);
  }

  @Transactional
  public void deleteByEntreprise(Entreprise entreprise) {
    telephoneRepository.deleteByEntreprise(entreprise);
  }

}
