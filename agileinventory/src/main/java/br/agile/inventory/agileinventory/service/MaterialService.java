package br.agile.inventory.agileinventory.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.agile.inventory.agileinventory.model.Material;
import br.agile.inventory.agileinventory.repository.MaterialRepository;

@Service
public class MaterialService {

    MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public Material getOrCreateMaterial(String code, String description) {
        return materialRepository.findByCode(code).orElseGet(() -> {
            Material material = new Material();
            material.setCode(code);
            material.setDescription(description);
            return materialRepository.save(material);
        });
    }

    public List<Material> getFirst20Materials() {
        return materialRepository.findFirst20By();
    }

    public Optional<Material> findById(Long id) {
        return materialRepository.findById(id);
    }
}
