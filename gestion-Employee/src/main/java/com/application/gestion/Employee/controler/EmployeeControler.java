package com.application.gestion.Employee.controler;

import com.application.gestion.Employee.model.Employee;
import com.application.gestion.Employee.model.Entreprise;
import com.application.gestion.Employee.model.Telephone;
import com.application.gestion.Employee.service.EmployeeService;
import com.application.gestion.Employee.service.EntrepriseService;
import com.application.gestion.Employee.service.PdfService;
import com.application.gestion.Employee.service.TelephoneService;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import org.thymeleaf.context.Context;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Controller
@AllArgsConstructor
public class EmployeeControler {

    private final EmployeeService employeeService;
    private final TelephoneService telephoneService;
    private final EntrepriseService entrepriseService;
    private final PdfService pdfService;

  @GetMapping("/employees")
    public String getAllEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "list";
    }

  @GetMapping("/employees/filter")
  public String filterAndSortEmployees(
      @RequestParam(name = "filterBy", required = false) String filterBy,
      @RequestParam(name = "orderBy", required = false) String orderBy,
      @RequestParam(name = "keyword", required = false) String keyword,
      Model model) {

    List<Employee> employees;

    boolean ascendingOrder = orderBy != null && !orderBy.isEmpty() && orderBy.equalsIgnoreCase("asc");

    if ("sexe".equalsIgnoreCase(filterBy) && keyword != null && !keyword.isEmpty()) {
      // Filtre par sexe en utilisant le mot-clé saisi (insensible à la casse)
      employees = employeeService.filterEmployeesBySexe(keyword);
    }else if ("dateEmbauche".equalsIgnoreCase(filterBy)) {
      if (keyword != null && !keyword.isEmpty()) {
        try {
          LocalDate dateEmbauche = LocalDate.parse(keyword);
          employees = employeeService.filterEmployeesByDateEmbauche(dateEmbauche);
        } catch (DateTimeParseException e) {
          // Gérer l'erreur de conversion de la date
          e.printStackTrace();
          employees = employeeService.getAllEmployees();
        }
      } else {
        employees = ascendingOrder ? employeeService.filterEmployeesByDateEmbaucheAsc() : employeeService.filterEmployeesByDateEmbaucheDesc();
      }
    } else if ("dateDepart".equalsIgnoreCase(filterBy)) {
      if (keyword != null && !keyword.isEmpty()) {
        try {
          LocalDate dateDepart = LocalDate.parse(keyword);
          employees = employeeService.filterEmployeesByDateDepart(dateDepart);
        } catch (DateTimeParseException e) {
          // Gérer l'erreur de conversion de la date
          e.printStackTrace();
          employees = employeeService.getAllEmployees();
        }
      } else {
        employees = ascendingOrder ? employeeService.filterEmployeesByDateDepartAsc() : employeeService.filterEmployeesByDateDepartDesc();
      }
    }else if ("name".equalsIgnoreCase(filterBy)) {
      if (ascendingOrder) {
        employees = employeeService.filterEmployeesByNameAsc(keyword);
      } else if (orderBy != null && orderBy.equalsIgnoreCase("desc")) {
        employees = employeeService.filterEmployeesByNameDesc(keyword);
      } else {
        employees = employeeService.filterEmployeesByName(keyword);
      }
    } else if ("firstname".equalsIgnoreCase(filterBy)) {
      if (ascendingOrder) {
        employees = employeeService.filterEmployeesByFirstnameAsc(keyword);
      } else if (orderBy != null && orderBy.equalsIgnoreCase("desc")) {
        employees = employeeService.filterEmployeesByFirstnameDesc(keyword);
      } else {
        employees = employeeService.filterEmployeesByFirstname(keyword);
      }
    } else if ("fonction".equalsIgnoreCase(filterBy)) {
      if (ascendingOrder) {
        employees = employeeService.filterEmployeesByFonctionAsc(keyword);
      } else if (orderBy != null && orderBy.equalsIgnoreCase("desc")) {
        employees = employeeService.filterEmployeesByFonctionDesc(keyword);
      } else {
        employees = employeeService.filterEmployeesByFonction(keyword);
      }
    } else if ("telephones".equalsIgnoreCase(filterBy)) {
      if (keyword != null && !keyword.isEmpty()) {
        // Utilise le mot-clé "keyword" pour filtrer les employés par téléphone
        employees = employeeService.filterEmployeesByTelephones(keyword);
      } else {
        employees = employeeService.getAllEmployees();
      }
    }else if (orderBy != null && !orderBy.isEmpty()) {
      String methodName = "filterEmployeesBy" + StringUtils.capitalize(filterBy);
      if (ascendingOrder) {
        methodName += "Asc";
      } else if (orderBy.equalsIgnoreCase("desc")) {
        methodName += "Desc";
      }

      try {
        Method method;
        if (keyword != null && !keyword.isEmpty()) {
          // Utilise le mot-clé "keyword" pour filtrer les employés en plus du tri
          method = EmployeeService.class.getMethod(methodName, String.class);
          employees = (List<Employee>) method.invoke(employeeService, keyword);
        } else {
          // Utilise la méthode sans paramètre du service pour trier sans filtre lorsque le mot-clé est vide
          method = EmployeeService.class.getMethod(methodName);
          employees = (List<Employee>) method.invoke(employeeService);
        }
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        // Gérer l'erreur appropriée
        e.printStackTrace();
        employees = employeeService.getAllEmployees();
      }
    } else {
      employees = employeeService.getAllEmployees();
    }

    model.addAttribute("employees", employees);
    return "list";
  }

  @GetMapping("employees/{id}/generate-pdf")
  public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
    try {
      Employee employee = employeeService.getEmployeeById(id);
      List<Entreprise> entreprises = entrepriseService.getAllEntreprises();

      byte[] pdfContent = pdfService.generatePdf(employee, entreprises);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_PDF);
      headers.setContentDispositionFormData("inline", "fiche_employe.pdf");

      return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while generating the PDF.".getBytes());
    }
  }



  @GetMapping("/employees/new")
    public String createEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "add";
    }

    @PostMapping("/employees")
    public String createEmployee(@ModelAttribute("employee") Employee employee,
                                 @RequestParam("image") MultipartFile imageFile,
                                 @RequestParam("telephones") String telephoneNumbers, Model model) {

        // Créer l'employé
        Employee savedEmployee = employeeService.createEmployee(employee);
        // Séparer les numéros de téléphone en utilisant la virgule comme délimiteur
        String[] numbersArray = telephoneNumbers.split(",");

      // Enregistrer chaque numéro de téléphone dans la base de données et le lier à l'employé
      for (String phoneNumber : numbersArray) {
        Telephone telephone = new Telephone(phoneNumber.trim(), savedEmployee);
        telephoneService.saveTelephone(telephone);
      }

        if (!imageFile.isEmpty()) {
            try {
                byte[] imageBytes = imageFile.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                employee.setPhoto(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage",
                        "Une erreur s'est produite lors du téléchargement de l'image. Veuillez réessayer.");
                return "add";
            }
        }

        return "redirect:/employees";
    }

    @GetMapping("/employees/{id}/edit")
    public String updateEmployeeForm(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "edit";
    }

    @PostMapping("/employees/{id}/update")
    public String updateEmployee(@PathVariable("id") Long id, @ModelAttribute("employee") Employee updatedEmployee,
                                 @RequestParam("image") MultipartFile imageFile,
                                 @RequestParam("telephones") String telephoneNumbers, Model model) {
        Employee employee = employeeService.getEmployeeById(id);

      // Copier les nouveaux numéros de téléphone à l'employé
      employee.setTelephones(updatedEmployee.getTelephones());

      // Supprimer les anciens numéros de téléphone liés à l'employé
      telephoneService.deleteByEmployee(employee);

        String[] numbersArray = telephoneNumbers.split(",");

      // Enregistrer chaque nouveau numéro de téléphone dans la base de données et le lier à l'employé
      for (String phoneNumber : numbersArray) {
        Telephone telephone = new Telephone(phoneNumber.trim(), employee);
        telephoneService.saveTelephone(telephone);
      }

        // Récupérer le matricule existant
        String matricule = employee.getMatricule();

        // Mettre à jour les autres champs de l'employé
        employee.setName(updatedEmployee.getName());
        employee.setFirstname(updatedEmployee.getFirstname());
        employee.setBirthdate(updatedEmployee.getBirthdate());
        employee.setSexe(updatedEmployee.getSexe());
        employee.setAdresse(updatedEmployee.getAdresse());
        employee.setEmailPerso(updatedEmployee.getEmailPerso());
        employee.setEmailPro(updatedEmployee.getEmailPro());
        employee.setCinNumero(updatedEmployee.getCinNumero());
        employee.setCinDateDelivrance(updatedEmployee.getCinDateDelivrance());
        employee.setCinLieuDelivrance(updatedEmployee.getCinLieuDelivrance());
        employee.setFonction(updatedEmployee.getFonction());
        employee.setNombreEnfants(updatedEmployee.getNombreEnfants());
        employee.setDateEmbauche(updatedEmployee.getDateEmbauche());
        employee.setDateDepart(updatedEmployee.getDateDepart());
        employee.setCategorieSocioPro(updatedEmployee.getCategorieSocioPro());
        employee.setNumeroCnaps(updatedEmployee.getNumeroCnaps());
        employee.setSalaireBrut(updatedEmployee.getSalaireBrut());
        // Traitement de la photo
        if (!imageFile.isEmpty()) {
            try {
                byte[] imageBytes = imageFile.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                employee.setPhoto(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage",
                        "Une erreur s'est produite lors du téléchargement de l'image. Veuillez réessayer.");
                return "fiche";
            }
        }
        // Réaffecter le matricule existant
        employee.setMatricule(matricule);

        employeeService.updateEmployee(employee);
        return "redirect:/employees/" + id + "/fiche";
    }


    @GetMapping("/employees/{id}/delete")
    public String deleteEmployee(@PathVariable("id") Long id) {
      employeeService.deleteEmployeeAndPhones(id);
      return "redirect:/employees";
    }



  @GetMapping("/employees/{id}/fiche")
    public String getEmployeeFiche(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);

         // Récupérez les informations de l'entreprise
        List<Entreprise> entreprises = entrepriseService.getAllEntreprises();
        model.addAttribute("entreprises", entreprises);
        return "fiche";
    }

    @GetMapping("/employees/{id}/uploadImage")
    public String uploadImage(@PathVariable("id") Long id, @RequestParam("image") MultipartFile imageFile, Model model) {
        if (!imageFile.isEmpty()) {
            try {
                byte[] imageBytes = imageFile.getBytes();

                // Convertir les données de l'image en Base64
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                // Mettre à jour le champ correspondant dans l'entité Employee
                employeeService.updateEmployeeImage(id, base64Image);

            } catch (IOException e) {
                e.printStackTrace();

                // Afficher un message d'erreur à l'utilisateur
                model.addAttribute("errorMessage", "Une erreur s'est produite lors du téléchargement de l'image. Veuillez réessayer.");

                // Rediriger vers la vue appropriée pour afficher le message d'erreur
                return "fiche";
            }
        }

        // Rediriger vers la page de détails de l'employé
        return "redirect:/employees/" + id + "/list";
    }




}
