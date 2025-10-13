package br.agile.inventory.agileinventory.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import br.agile.inventory.agileinventory.dto.OrderRequest;
import br.agile.inventory.agileinventory.model.Order;
import br.agile.inventory.agileinventory.service.OrderService;
import br.agile.inventory.agileinventory.util.export.OrderExcelReporter;

@Controller
@RequestMapping("/orders")
public class OrderController {
    
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String orders(Model model, RedirectAttributes redirectAttributes) {
        List<Order> orders = orderService.getFirst20Orders();

        if (orders.isEmpty()) {
            model.addAttribute("message", "Nenhuma Ordem de Produção encontrada.");
        } else {
            model.addAttribute("orders", orders);
        }
    
        return "orders";
    }

    @GetMapping("/search")
    public String searchOrders(@RequestParam("orderId") Long orderId, Model model, RedirectAttributes redirectAttributes) {
        Order existOrder = orderService.findByOrderId(orderId).orElse(null);

        if (existOrder != null) {
            redirectAttributes.addFlashAttribute("warning", "A Ordem de Produção de ID: " + orderId + " já cadastrada.");
            return "redirect:/orders";
        }
        
        return "redirect:/orders/add/" + orderId;
    }
    
    @GetMapping("/add/{orderId}")
    public String addOrder(@PathVariable("orderId") Long orderId, Model model, RedirectAttributes redirectAttributes) {
        String url = "http://localhost:8000/scraping/" + orderId;
        RestTemplate restTemplate = new RestTemplate();

        try {
            OrderRequest order = restTemplate.getForObject(url, OrderRequest.class);

            if (order == null) {
                redirectAttributes.addFlashAttribute("error", "Nenhum dado encontrado para a ordem " + orderId);
                return "redirect:/orders";
            }

            if (order.getMaterials().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Nenhum material encontrado para a ordem ID:" + orderId + " " + order.getOrderNumber());
                return "redirect:/orders";
            }

            model.addAttribute("order", order);

            return "add-order";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao buscar dados da ordem de produção: " + e.getMessage());
            return "redirect:/orders";
        }
    }

    
    @PostMapping
    public String createOrder(OrderRequest request, Model model, RedirectAttributes redirectAttributes) {
        try {
            orderService.createOrder(request);
            redirectAttributes.addFlashAttribute("success", "Ordem de Produção cadastrada com sucesso.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String editOrder(@PathVariable("id") Long id, Model model) {
        OrderRequest order = orderService.findById(id);

        model.addAttribute("order", order);
        return "edit-order";
    }

    @PostMapping("/update/{id}")
    public String updateOrder(@PathVariable("id") Long id, OrderRequest request, RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrder(id, request);
            redirectAttributes.addFlashAttribute("success", "Ordem de Produção atualizada com sucesso.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar Ordem de Produção: " + e.getMessage());
        }
        return "redirect:/orders";
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportOrdersToExcel() throws IOException {
        List<OrderRequest> orders = orderService.getAllOrdersForExport();
        byte[] excelFile = OrderExcelReporter.exportOrders(orders);

        String fileName = "ordens_producao_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy")) + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
    }

}
