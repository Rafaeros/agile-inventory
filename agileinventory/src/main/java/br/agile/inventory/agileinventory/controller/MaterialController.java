package br.agile.inventory.agileinventory.controller;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.agile.inventory.agileinventory.dto.MaterialRequest;
import br.agile.inventory.agileinventory.model.Material;
import br.agile.inventory.agileinventory.service.MaterialService;

@Controller
@RequestMapping("/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }
    
    @GetMapping
    public String materials(Model model) {
        List<Material> materials = materialService.getFirst20Materials();

        if (materials.isEmpty()) {
            model.addAttribute("message", "Nenhum material encontrado.");
        } else {
            model.addAttribute("materials", materials);
        }

        return "materials";
    }

    @PostMapping
    public String createMaterial(@RequestBody MaterialRequest request) {
        materialService.getOrCreateMaterial(request.getCode(), request.getDescription());
        return "redirect:/materials";
    }

}
