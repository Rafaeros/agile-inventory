package br.agile.inventory.agileinventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // <-- Is @Controller present?
public class HomeController {


    @GetMapping("/") // <-- Is the mapping exactly "/"?
    public String home(Model model) {
        model.addAttribute("message", "Olá do Spring Boot com JSP!");
        return "home";
    }
}