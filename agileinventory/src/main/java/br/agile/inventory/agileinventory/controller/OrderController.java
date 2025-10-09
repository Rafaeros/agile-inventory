package br.agile.inventory.agileinventory.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.agile.inventory.agileinventory.dto.OrderMaterialRequest;
import br.agile.inventory.agileinventory.dto.OrderRequest;
import br.agile.inventory.agileinventory.model.Order;
import br.agile.inventory.agileinventory.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {
    
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String orders(Model model) {
        List<Order> orders = orderService.getFirst20Orders();

        if (orders.isEmpty()) {
            model.addAttribute("message", "Nenhuma Ordem de Produção encontrada.");
        } else {
            model.addAttribute("orders", orders);
        }

        return "orders";
    }

    @PostMapping
    public String createOrder(@RequestBody OrderRequest request, Model model) {
        System.out.println(request.getOrderId());
        System.out.println(request.getOrderNumber());
        System.out.println(request.getCode());
        System.out.println(request.getDescription());
        System.out.println(request.getQuantity());
        
        for(OrderMaterialRequest material : request.getMaterials()) {
            System.out.println(material.getCode());
            System.out.println(material.getDescription());
            System.out.println(material.getQuantity());
        }

        try {
            orderService.createOrder(request);
            model.addAttribute("success", "Ordem de Produção cadastrada com sucesso.");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/orders";
    }

}
