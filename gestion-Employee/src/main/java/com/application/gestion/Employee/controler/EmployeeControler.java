package com.application.gestion.Employee.controler;

import com.application.gestion.Employee.model.Employee;
import com.application.gestion.Employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;

@Controller
@RequiredArgsConstructor
public class EmployeeControler {

    private final EmployeeService employeeService;

    @GetMapping("/employees")
    public String getAllEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "list";
    }

    @GetMapping("/employees/new")
    public String createEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "add";
    }

    @PostMapping("/employees")
    public String createEmployee(@ModelAttribute("employee") Employee employee,
                                 @RequestParam("image") MultipartFile imageFile, Model model) {
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

        employeeService.createEmployee(employee);
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
                                 @RequestParam("image") MultipartFile imageFile, Model model) {
        Employee employee = employeeService.getEmployeeById(id);

        // Récupérer le matricule existant
        String matricule = employee.getMatricule();

        // Mettre à jour les autres champs de l'employé
        employee.setName(updatedEmployee.getName());
        employee.setFirstname(updatedEmployee.getFirstname());
        employee.setBirthdate(updatedEmployee.getBirthdate());

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
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }


    @GetMapping("/employees/{id}/fiche")
    public String getEmployeeFiche(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
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
        return "redirect:/employees/" + id + "/fiche";
    }




}

