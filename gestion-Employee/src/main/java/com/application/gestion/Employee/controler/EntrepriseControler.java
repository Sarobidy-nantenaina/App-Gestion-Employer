package com.application.gestion.Employee.controler;

import com.application.gestion.Employee.model.Entreprise;
import com.application.gestion.Employee.model.Telephone;
import com.application.gestion.Employee.service.EntrepriseService;
import com.application.gestion.Employee.service.TelephoneService;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@AllArgsConstructor
public class EntrepriseControler {

  private final EntrepriseService entrepriseService;
  private final TelephoneService telephoneService;

  @GetMapping("/entreprises")
  public String getAllEntreprise(Model model) {
    model.addAttribute("entreprises", entrepriseService.getAllEntreprises());
    return "list_entreprise";
  }

  @GetMapping("/home")
  public String accueil(Model model) {
    // Récupérer l'entreprise ayant l'id égal à 1 et l'ajouter au modèle
    Entreprise entreprise1 = entrepriseService.getEntrepriseWithId1();
    model.addAttribute("entreprise1", entreprise1);

    return "home";
  }

  @GetMapping("/entreprises/new")
  public String createEntrepriseForm(Model model) {
    model.addAttribute("entreprise", new Entreprise());
    return "add_entreprise";
  }

  @PostMapping("/entreprises")
  public String createEntreprise(@ModelAttribute("entreprise") Entreprise entreprise,
                                 @RequestParam("image") MultipartFile imageFile,
                                 @RequestParam("telephones") String telephoneNumbers, Model model) {

    // Créer l'entreprise
    Entreprise savedEntreprise = entrepriseService.createEntreprise(entreprise);
    // Séparer les numéros de téléphone en utilisant la virgule comme délimiteur
    String[] numbersArray = telephoneNumbers.split(",");

    // Enregistrer chaque numéro de téléphone dans la base de données et le lier à l'entreprise
    for (String phoneNumber : numbersArray) {
      Telephone telephone = new Telephone(phoneNumber.trim(), savedEntreprise);
      telephoneService.saveTelephone(telephone);
    }

    if (!imageFile.isEmpty()) {
      try {
        byte[] imageBytes = imageFile.getBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        entreprise.setLogo(base64Image);
      } catch (IOException e) {
        e.printStackTrace();
        model.addAttribute("errorMessage",
            "Une erreur s'est produite lors du téléchargement de l'image. Veuillez réessayer.");
        return "add_entreprise";
      }
    }

    try {
      // Le code actuel pour créer l'entreprise et le traitement de l'image ici...

      return "redirect:/entreprises";
    } catch (Exception e) {
      e.printStackTrace();
      model.addAttribute("errorMessage",
          "Une erreur s'est produite lors de la création de l'entreprise. Veuillez réessayer.");
      return "add_entreprise";
    }
  }

  @GetMapping("/entreprises/{id}/edit")
  public String editEntrepriseForm(@PathVariable("id") Long id, Model model) {
    Entreprise entreprise = entrepriseService.getEntrepriseById(id);
    model.addAttribute("entreprise", entreprise);
    return "edit_entreprise";
  }
  @PostMapping("/entreprises/{id}/update")
  public String updateEntreprise(@PathVariable("id") Long id, @ModelAttribute("entreprise") Entreprise updatedEntreprise,
                                 @RequestParam("image") MultipartFile imageFile,
                                 @RequestParam("telephones") String telephoneNumbers, Model model) {
    Entreprise entreprise = entrepriseService.getEntrepriseById(id);

    // Copier les nouveaux numéros de téléphone à l'entreprise
    entreprise.setTelephones(updatedEntreprise.getTelephones());

    // Supprimer les anciens numéros de téléphone liés à l'entreprise
    telephoneService.deleteByEntreprise(entreprise);

    String[] numbersArray = telephoneNumbers.split(",");

    // Enregistrer chaque nouveau numéro de téléphone dans la base de données et le lier à l'entreprise
    for (String phoneNumber : numbersArray) {
      Telephone telephone = new Telephone(phoneNumber.trim(), entreprise);
      telephoneService.saveTelephone(telephone);
    }

    // Mettre à jour les autres champs de l'entreprise
    entreprise.setNom(updatedEntreprise.getNom());
    entreprise.setDescription(updatedEntreprise.getDescription());
    entreprise.setSlogan(updatedEntreprise.getSlogan());
    entreprise.setAdresse(updatedEntreprise.getAdresse());
    entreprise.setEmailContact(updatedEntreprise.getEmailContact());
    entreprise.setNif(updatedEntreprise.getNif());
    entreprise.setStat(updatedEntreprise.getStat());
    entreprise.setRcs(updatedEntreprise.getRcs());

    // Traitement du logo
    if (!imageFile.isEmpty()) {
      try {
        byte[] imageBytes = imageFile.getBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        entreprise.setLogo(base64Image);
      } catch (IOException e) {
        e.printStackTrace();
        model.addAttribute("errorMessage",
            "Une erreur s'est produite lors du téléchargement du logo. Veuillez réessayer.");
        return "list_entreprise";
      }
    }

    entrepriseService.updateEntreprise(entreprise);
    return "redirect:/entreprises";
  }

  @GetMapping("/entreprises/{id}/uploadImage")
  public String uploadImage(@PathVariable("id") Long id, @RequestParam("image") MultipartFile imageFile, Model model) {
    if (!imageFile.isEmpty()) {
      try {
        byte[] imageBytes = imageFile.getBytes();

        // Convertir les données de l'image en Base64
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        // Mettre à jour le champ correspondant dans l'entité Employee
        entrepriseService.updateEntrepriseLogo(id, base64Image);

      } catch (IOException e) {
        e.printStackTrace();

        // Afficher un message d'erreur à l'utilisateur
        model.addAttribute("errorMessage", "Une erreur s'est produite lors du téléchargement de l'image. Veuillez réessayer.");

        // Rediriger vers la vue appropriée pour afficher le message d'erreur
        return "list_entreprise";
      }
    }

    // Rediriger vers la page de détails de l'employé
    return "redirect:/entreprises";
  }


  @GetMapping("/entreprises/{id}/delete")
  public String deleteEmployee(@PathVariable("id") Long id) {
    entrepriseService.deleteEntrepriseAndPhones(id);
    return "redirect:/entreprises";
  }


}
