package br.agile.inventory.agileinventory.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.agile.inventory.agileinventory.dto.ProductRequest;
import br.agile.inventory.agileinventory.model.Product;
import br.agile.inventory.agileinventory.service.ProductService;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public String products(Model model) {
        List<Product> products = productService.getFirst20Products();

        if (products.isEmpty()) {
            model.addAttribute("message", "Nenhum produto encontrado.");
        } else {
            model.addAttribute("products", products);
        }

        return "products";
    }

    @PostMapping
    public String createProduct(@RequestBody ProductRequest request) {
        productService.getOrCreateProduct(request.getCode(), request.getDescription());
        return "redirect:/products";
    }

    

}
