package br.agile.inventory.agileinventory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import br.agile.inventory.agileinventory.repository.ProductRepository;

import br.agile.inventory.agileinventory.model.Product;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getOrCreateProduct(String code, String description) {
        return productRepository.findByCode(code).orElseGet(() -> {
                Product product = new Product();
                product.setCode(code);
                product.setDescription(description);
                return productRepository.save(product);
            }
        );
    }

    public List<Product> getFirst20Products() {
        return productRepository.findFirst20By();
    }

}
