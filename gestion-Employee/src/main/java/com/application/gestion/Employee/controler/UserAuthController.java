package com.application.gestion.Employee.controler;

import com.application.gestion.Employee.model.UserAuth;
import com.application.gestion.Employee.service.UserAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class UserAuthController {

  private final UserAuthService userAuthService;

  @GetMapping("/")
  public String showDefaultPage() {
    return "redirect:/signup"; // Redirige vers la page d'inscription par défaut
  }

  @GetMapping("/signup")
  public String showSignupPage(Model model) {
    model.addAttribute("user", new UserAuth());
    return "signup"; // Redirige vers la page d'inscription Thymeleaf signup.html
  }

  @PostMapping("/signup")
  public String processSignupForm(@ModelAttribute("user") UserAuth user, HttpSession session) {
    userAuthService.saveUser(user);
    session.setAttribute("user", user);
    return "redirect:/home"; // Redirige vers la page d'accueil après l'inscription réussie.
  }

  @GetMapping("/signin")
  public String showSigninPage(Model model) {
    model.addAttribute("user", new UserAuth());
    return "signin"; // Redirige vers la page de connexion Thymeleaf signin.html
  }

  @PostMapping("/signin")
  public String processSigninForm(@ModelAttribute("user") UserAuth user, HttpSession session) {
    UserAuth existingUser = userAuthService.getUserByUsername(user.getFirstname());
    if (existingUser != null && userAuthService.checkPassword(existingUser, user.getPassword())) {
      session.setAttribute("user", existingUser);
      return "redirect:/home"; // Redirige vers la page d'accueil après la connexion réussie.
    } else {
      return "redirect:/signin?error=true"; // Redirige vers la page de connexion avec un message d'erreur.
    }
  }

  // Reste du contrôleur pour gérer les autres pages et fonctionnalités...

}

