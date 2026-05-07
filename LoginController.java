package com.example.onlinebookstore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @GetMapping("/login-choice")
    public String showLoginChoice() {
        return "login-choice";
    }

    @GetMapping("/login/{role}")
    public String showLoginForm(@PathVariable String role, Model model) {
        model.addAttribute("role", role.toUpperCase());
        return "login";
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestParam String username, 
                               @RequestParam String password, 
                               @RequestParam String role, 
                               HttpSession session, 
                               Model model) {
        
        // Simple hardcoded authentication for demonstration
        if ("admin".equalsIgnoreCase(role)) {
            if ("admin".equals(username) && "admin123".equals(password)) {
                session.setAttribute("userRole", "ADMIN");
                session.setAttribute("username", "Administrator");
                return "redirect:/books";
            }
        } else if ("user".equalsIgnoreCase(role)) {
            // Users don't need a password for this simple demo, or use a default one
            if ("user".equals(username) && "user123".equals(password)) {
                session.setAttribute("userRole", "USER");
                session.setAttribute("username", username);
                return "redirect:/books";
            }
        }
        
        model.addAttribute("error", "Invalid credentials for " + role + " role.");
        model.addAttribute("role", role.toUpperCase());
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
