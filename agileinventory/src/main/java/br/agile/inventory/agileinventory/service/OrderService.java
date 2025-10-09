package br.agile.inventory.agileinventory.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import br.agile.inventory.agileinventory.repository.OrderRepository;
import jakarta.transaction.Transactional;

import br.agile.inventory.agileinventory.dto.OrderMaterialRequest;
import br.agile.inventory.agileinventory.dto.OrderRequest;
import br.agile.inventory.agileinventory.model.Order;
import br.agile.inventory.agileinventory.model.OrderMaterial;
import br.agile.inventory.agileinventory.model.Product;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMaterialService orderMaterialService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, OrderMaterialService orderMaterialService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderMaterialService = orderMaterialService;
        this.productService = productService;
    }

    public List<Order> getFirst20Orders() {
        return orderRepository.findFirst20By();
    }

    @Transactional
    public Order createOrder(OrderRequest request) {
        Optional<Order> order = orderRepository.findByOrderId(request.getOrderId());

        if (order.isPresent()) {
            throw new RuntimeException("Ordem de Produção com o ID " + request.getOrderId() + " já cadastrada.");
        }

        Product product = productService.getOrCreateProduct(request.getCode(), request.getDescription());

        Order newOrder = new Order();
        newOrder.setOrderId(request.getOrderId());
        newOrder.setOrderNumber(request.getOrderNumber());
        newOrder.setQuantity(request.getQuantity());
        newOrder.setProduct(product);
        newOrder = orderRepository.save(newOrder);

        for (OrderMaterialRequest orderMaterialRequest : request.getMaterials()) {
            OrderMaterial orderMaterial = orderMaterialService.createOrderMaterial(orderMaterialRequest.getCode(), orderMaterialRequest.getDescription(), orderMaterialRequest.getQuantity(), newOrder);
            newOrder.getMaterials().add(orderMaterial);
        }

        return newOrder;
    }
}
