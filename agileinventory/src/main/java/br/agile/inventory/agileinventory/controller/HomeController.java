package br.agile.inventory.agileinventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Olá do Spring Boot com JSP!");
        return "home";
    }
}