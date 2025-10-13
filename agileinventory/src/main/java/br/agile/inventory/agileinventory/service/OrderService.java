package br.agile.inventory.agileinventory.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import br.agile.inventory.agileinventory.repository.OrderRepository;
import br.agile.inventory.agileinventory.util.OrderMapper;
import jakarta.transaction.Transactional;
import br.agile.inventory.agileinventory.dto.OrderMaterialRequest;
import br.agile.inventory.agileinventory.dto.OrderRequest;
import br.agile.inventory.agileinventory.model.Material;
import br.agile.inventory.agileinventory.model.Order;
import br.agile.inventory.agileinventory.model.OrderMaterial;
import br.agile.inventory.agileinventory.model.Product;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMaterialService orderMaterialService;
    private final ProductService productService;
    private final MaterialService materialService;

    public OrderService(OrderRepository orderRepository, OrderMaterialService orderMaterialService, ProductService productService, MaterialService materialService) {
        this.orderRepository = orderRepository;
        this.orderMaterialService = orderMaterialService;
        this.productService = productService;
        this.materialService = materialService;
    }

    public OrderRequest findById(Long id) {
        Order order = orderRepository.findByIdWithDetails(id).orElseThrow(() -> new RuntimeException("Ordem de Produção com o ID " + id + " nao encontrada."));
        OrderRequest orderReq = OrderMapper.toDto(order);
        return orderReq;
    }  

    public Optional<Order> findByOrderId(Long orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    public Order findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber).orElse(null);
    }

    public List<Order> findOrdersByProductCode(String code) {
        return orderRepository.findOrdersByProductCode(code);
    }

    public List<Order> findOrdersByProductCodeWithMaterials(String code) {
        return orderRepository.findOrdersByProductCodeWithMaterials(code);
    }

    public List<Order> getFirst20Orders() {
        return orderRepository.findFirst20By();
    }
    
    public List<OrderRequest> getAllOrdersForExport() {
        List<Order> orders = orderRepository.findAllWithDetails();
        return orders.stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderRequest updateOrder(Long id, OrderRequest request) {
        try {
            Order order = orderRepository.findByIdWithDetails(id).orElseThrow(() -> new RuntimeException("Ordem de Produção com o ID " + id + " nao encontrada."));
            order.setOrderId(request.getOrderId());
            order.setOrderNumber(request.getOrderNumber());
            order.setQuantity(request.getQuantity());
            
            List<OrderMaterial> updatedMaterials = new ArrayList<>();
            for (OrderMaterialRequest omReq : request.getMaterials()) {
                if (omReq.getMaterialId() == null) {
                    throw new RuntimeException("O campo material_id não pode ser nulo.");
                }
                OrderMaterial om;

                if (omReq.getId() != null) {
                    om = order.getMaterials().stream()
                    .filter(m -> m.getId().equals(omReq.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("OrderMaterial com ID " + omReq.getId() + " nao encontrado."));
                } else {
                    om = new OrderMaterial();
                }
                
                Material material = materialService.findById(omReq.getMaterialId()).orElseThrow(() -> new RuntimeException("Material com o ID " + omReq.getMaterialId() + " nao encontrado."));
                
                om.setMaterial(material);
                om.setOrder(order);
                om.setQuantity(omReq.getQuantity());
                
                updatedMaterials.add(om);
            }
            order.getMaterials().clear();
            order.getMaterials().addAll(updatedMaterials);
            orderRepository.save(order);
            return OrderMapper.toDto(order);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar ordem de produção: " + e.getMessage());
        }
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
