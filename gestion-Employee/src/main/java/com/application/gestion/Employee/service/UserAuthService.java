package com.application.gestion.Employee.service;

import com.application.gestion.Employee.CustomHasher;
import com.application.gestion.Employee.model.UserAuth;
import com.application.gestion.Employee.repository.UserAuthRepository;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.stereotype.Service;
@Service
public class UserAuthService {

  private final UserAuthRepository userAuthRepository;
  private final CustomHasher customHasher; // Injectez le CustomHasher

  public UserAuthService(UserAuthRepository userAuthRepository, CustomHasher customHasher) {
    this.userAuthRepository = userAuthRepository;
    this.customHasher = customHasher;
  }
  public UserAuth getUserById(Long userId) {
    return userAuthRepository.findById(userId).orElse(null);
  }

  public UserAuth getUserByUsername(String username) {
    return userAuthRepository.findByFirstname(username);
  }

  public void saveUser(UserAuth user) {
    // Assurez-vous de hacher le mot de passe avant de l'enregistrer dans la base de données
    Hash hashedPassword = customHasher.hash(user.getPassword(), user.getFirstname()); // Utilisez le CustomHasher
    user.setPassword(hashedPassword.toString());

    userAuthRepository.save(user);
  }

  public void updateUser(UserAuth user) {
    userAuthRepository.save(user);
  }

  public void deleteUser(Long userId) {
    userAuthRepository.deleteById(userId);
  }

  public List<UserAuth> getAllUsers() {
    return userAuthRepository.findAll();
  }

  // Méthode pour obtenir l'utilisateur actuellement connecté à partir de la session
  public UserAuth getCurrentUser(HttpSession session) {
    return (UserAuth) session.getAttribute("user");
  }

  // Méthode pour vérifier les informations d'authentification de l'utilisateur
  public boolean checkPassword(UserAuth user, String password) {
    // Obtenez le mot de passe haché de l'utilisateur
    String hashedPassword = user.getPassword();

    // Vérifiez le mot de passe en utilisant le CustomHasher
    Hash hashedPasswordHashed = customHasher.hash(password, user.getFirstname());

    // Comparez le mot de passe haché saisi avec le mot de passe haché de l'utilisateur dans la base de données
    return hashedPassword.equals(hashedPasswordHashed.toString());
  }
}
