package br.agile.inventory.agileinventory.controller;


import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.agile.inventory.agileinventory.repository.ProductionOrderRepository;
import br.agile.inventory.agileinventory.model.ProductionOrder;

@Controller
public class ProductionOrderController {

    private final ProductionOrderRepository productionOrderRepository;
    public ProductionOrderController(ProductionOrderRepository productionOrderRepository) {
        this.productionOrderRepository = productionOrderRepository;
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        List<ProductionOrder> orders = productionOrderRepository.findAllWithMaterials();

        if(orders.isEmpty()) {
            model.addAttribute("message", "Nenhuma ordem de produção cadastrada.");
        } else {
            model.addAttribute("orders", productionOrderRepository.findAllWithMaterials());
        }

        return "orders";
    }
}