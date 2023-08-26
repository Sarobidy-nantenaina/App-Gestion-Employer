package com.application.gestion.Employee.service;

import com.application.gestion.Employee.exception.EntrepriseNotFoundException;
import com.application.gestion.Employee.model.Employee;
import com.application.gestion.Employee.model.Entreprise;
import com.application.gestion.Employee.repository.EntrepriseRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntrepriseService {

  private final EntrepriseRepository entrepriseRepository;
  private final TelephoneService telephoneService;

  public List<Entreprise> getAllEntreprises() {
    return entrepriseRepository.findAll();
  }

  public Entreprise createEntreprise(Entreprise entreprise) {
    return entrepriseRepository.save(entreprise);
  }

  public Entreprise getEntrepriseById(Long id) {
    return entrepriseRepository.findById(id).orElse(null);
  }

  public Entreprise getEntrepriseWithId1() {
    return entrepriseRepository.findById(1L).orElse(null);
  }


  public Entreprise updateEntreprise(Entreprise entreprise) {
    return entrepriseRepository.save(entreprise);
  }

  public void updateEntrepriseLogo(Long entrepriseId, String base64Image) {
    Entreprise entreprise = entrepriseRepository.findById(entrepriseId)
        .orElseThrow(() -> new EntrepriseNotFoundException("Employee not found with ID: " + entrepriseId));

    entreprise.setLogo(base64Image);

    entrepriseRepository.save(entreprise);
  }


  @Transactional
  public void deleteEntrepriseAndPhones(Long id) {
    Entreprise entreprise = entrepriseRepository.findById(id).orElse(null);
    if (entreprise != null) {
      // Supprimer les numéros de téléphone associés à l'employé
      telephoneService.deleteByEntreprise(entreprise);
      // Supprimer l'employé lui-même
      entrepriseRepository.deleteById(id);
    }
  }

}
