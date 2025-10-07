package br.agile.inventory.agileinventory.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import br.agile.inventory.agileinventory.repository.ProductionOrderRepository;
import br.agile.inventory.agileinventory.model.ProductionOrder;

@Controller
@RequestMapping("/orders")
public class ProductionOrderController {

    private final ProductionOrderRepository productionOrderRepository;
    public ProductionOrderController(ProductionOrderRepository productionOrderRepository) {
        this.productionOrderRepository = productionOrderRepository;
    }

    @GetMapping
    public String orders(Model model) {
        List<ProductionOrder> orders = productionOrderRepository.findAllWithMaterials();

        if(orders.isEmpty()) {
            model.addAttribute("message", "Nenhuma ordem de produção cadastrada.");
        } else {
            model.addAttribute("orders", productionOrderRepository.findAllWithMaterials());
        }

        return "orders";
    }

    @PostMapping
    public ResponseEntity<ProductionOrder> createOrder(@RequestBody ProductionOrder productionOrder) {
        Optional<ProductionOrder> order = productionOrderRepository.findByOrderId(productionOrder.getOrderId());
        if (order.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // Retorna 409 se a ordem já existe
        }   
        
        // Etapa CRÍTICA: Definir a referência bidirecional
        if (productionOrder.getMaterials() != null) {
            productionOrder.getMaterials().forEach(material -> {
                material.setProductOrder(productionOrder); 
            });
        }
        
        ProductionOrder savedOrder = productionOrderRepository.save(productionOrder);
        
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }
    
    @GetMapping("/scrape")
    @ResponseBody
    public Map<String, Object> scrapeOrder(@RequestParam("orderId") String orderId, Model model) {
        Map<String, Object> result = new HashMap<>();

        Optional<ProductionOrder> order = productionOrderRepository.findByOrderId(Long.parseLong(orderId));
        if (order.isPresent()) {
            result.put("orderExists", true);
            result.put("orderMessage", "Ordem de produção já cadastrada.");
            return result;
        }
        String url = "http://localhost:8000/scraping/" + orderId;
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> data = response.getBody();
            result.put("orderExists", false);
            result.put("orderData", data);
        } catch (Exception e) {
            result.put("error", "Erro ao buscar dados da ordem de produção.");
        }

        return result;
    }
}