package br.agile.inventory.agileinventory.controller;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.agile.inventory.agileinventory.repository.MaterialRepository;
import br.agile.inventory.agileinventory.model.Material;

@Controller // <-- Is @Controller present?
public class MaterialController {

    private final MaterialRepository materialRepository;
    public MaterialController(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @GetMapping("/materials")
    public String materials(Model model) {
        List<Material> materials = materialRepository.findAllWithProductionOrder();

        if(materials.isEmpty()) {
            model.addAttribute("message", "Nenhum material cadastrado.");
        } else {
            model.addAttribute("materials", materialRepository.findAll());
        }

        return "materials";
        
    }
}