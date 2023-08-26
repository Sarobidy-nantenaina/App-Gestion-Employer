    package com.application.gestion.Employee.service;

    import com.application.gestion.Employee.exception.EmployeeNotFoundException;
    import com.application.gestion.Employee.model.Employee;
    import com.application.gestion.Employee.repository.EmployeeRepository;
    import jakarta.transaction.Transactional;
    import java.time.LocalDate;
    import java.util.Random;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class EmployeeService {

        private final EmployeeRepository employeeRepository;
        private final TelephoneService telephoneService;

        public List<Employee> getAllEmployees() {
            return employeeRepository.findAll();
        }

        public Employee getEmployeeById(Long id) {
            return employeeRepository.findById(id).orElse(null);
        }

        public Employee createEmployee(Employee employee) {
            employee.setMatricule(generateUniqueMatricule());
            return employeeRepository.save(employee);
        }

        public Employee updateEmployee(Employee employee) {
            return employeeRepository.save(employee);
        }


        @Transactional
        public void deleteEmployeeAndPhones(Long id) {
            Employee employee = employeeRepository.findById(id).orElse(null);
            if (employee != null) {
                // Supprimer les numéros de téléphone associés à l'employé
                telephoneService.deleteByEmployee(employee);
                // Supprimer l'employé lui-même
                employeeRepository.deleteById(id);
            }
        }


        public void updateEmployeeImage(Long employeeId, String base64Image) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + employeeId));

            employee.setPhoto(base64Image);

            employeeRepository.save(employee);
        }

        private String generateUniqueMatricule() {
            String matricule;
            do {
                matricule = generateRandomMatricule();
            } while (!isMatriculeUnique(matricule));
            return matricule;
        }

        private String generateRandomMatricule() {
            Random random = new Random();
            int matriculeNumber = random.nextInt(9000) + 1000; // Génère un nombre entre 1000 et 9999
            return "STD " + String.format("%04d", matriculeNumber); // Formatage avec des zéros à gauche
        }

        private boolean isMatriculeUnique(String matricule) {
            return !employeeRepository.existsByMatricule(matricule);
        }

        // Méthode pour filtrer les employés par nom sans tri
        public List<Employee> filterEmployeesByName(String name) {
            return employeeRepository.filterEmployeesByName(name);
        }

        // Méthode pour filtrer et trier les employés par nom en ordre ascendant
        public List<Employee> filterEmployeesByNameAsc(String name) {
            return employeeRepository.filterEmployeesByNameAsc(name);
        }

        // Méthode pour filtrer et trier les employés par nom en ordre descendant
        public List<Employee> filterEmployeesByNameDesc(String name) {
            return employeeRepository.filterEmployeesByNameDesc(name);
        }

        // Méthode pour filtrer les employés par prénom sans tri
        public List<Employee> filterEmployeesByFirstname(String firstname) {
            return employeeRepository.filterEmployeesByFirstname(firstname);
        }

        // Méthode pour filtrer et trier les employés par prénom en ordre ascendant
        public List<Employee> filterEmployeesByFirstnameAsc(String firstname) {
            return employeeRepository.filterEmployeesByFirstnameAsc(firstname);
        }

        // Méthode pour filtrer et trier les employés par prénom en ordre descendant
        public List<Employee> filterEmployeesByFirstnameDesc(String firstname) {
            return employeeRepository.filterEmployeesByFirstnameDesc(firstname);
        }

        // Méthode pour filtrer les employés par sexe
        public List<Employee> filterEmployeesBySexe(String sexe) {
            return employeeRepository.filterEmployeesBySexe(sexe.toLowerCase());
        }
        // Méthode pour filtrer les employés par fonction sans tri
        public List<Employee> filterEmployeesByFonction(String fonction) {
            return employeeRepository.filterEmployeesByFonction(fonction);
        }

        // Méthode pour filtrer et trier les employés par fonction en ordre ascendant
        public List<Employee> filterEmployeesByFonctionAsc(String fonction) {
            return employeeRepository.filterEmployeesByFonctionAsc(fonction);
        }

        // Méthode pour filtrer et trier les employés par fonction en ordre descendant
        public List<Employee> filterEmployeesByFonctionDesc(String fonction) {
            return employeeRepository.filterEmployeesByFonctionDesc(fonction);
        }

        // Méthode pour filtrer les employés par date d'embauche
        public List<Employee> filterEmployeesByDateEmbauche(LocalDate dateEmbauche) {
            return employeeRepository.filterEmployeesByDateEmbauche(dateEmbauche);
        }

        // Méthode pour filtrer et trier les employés par date d'embauche en ordre ascendant
        public List<Employee> filterEmployeesByDateEmbaucheAsc() {
            LocalDate dateEmbauche = LocalDate.now(); // Utiliser la date actuelle
            return employeeRepository.filterEmployeesByDateEmbaucheAsc(dateEmbauche);
        }

        public List<Employee> filterEmployeesByDateEmbaucheDesc() {
            LocalDate dateEmbauche = LocalDate.now(); // Utiliser la date actuelle
            return employeeRepository.filterEmployeesByDateEmbaucheDesc(dateEmbauche);
        }

            // Méthode pour filtrer les employés par date de départ
        public List<Employee> filterEmployeesByDateDepart(LocalDate dateDepart) {
            return employeeRepository.filterEmployeesByDateDepart(dateDepart);
        }

        // Méthode pour filtrer et trier les employés par date de départ en ordre ascendant
        public List<Employee> filterEmployeesByDateDepartAsc() {
            return employeeRepository.filterEmployeesByDateDepartAsc(LocalDate.now()); // Utiliser la date actuelle directement dans la requête
        }

        public List<Employee> filterEmployeesByDateDepartDesc() {
            return employeeRepository.filterEmployeesByDateDepartDesc(
                LocalDate.now()); // Utiliser la date actuelle directement dans la requête


        }

        // Méthode pour filtrer les employés par numéro de téléphone sans tri
        public List<Employee> filterEmployeesByTelephones(String telephone) {
            return employeeRepository.filterEmployeesByTelephones(telephone);
        }

        // Méthode pour filtrer et trier les employés par numéro de téléphone en ordre ascendant
        public List<Employee> filterEmployeesByTelephonesAsc(String telephone) {
            return employeeRepository.filterEmployeesByTelephonesAsc(telephone);
        }

        // Méthode pour filtrer et trier les employés par numéro de téléphone en ordre descendant
        public List<Employee> filterEmployeesByTelephonesDesc(String telephone) {
            return employeeRepository.filterEmployeesByTelephonesDesc(telephone);
        }
    }
