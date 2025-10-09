package br.agile.inventory.agileinventory.service;

import org.springframework.stereotype.Service;

import br.agile.inventory.agileinventory.model.Material;
import br.agile.inventory.agileinventory.model.Order;
import br.agile.inventory.agileinventory.model.OrderMaterial;
import br.agile.inventory.agileinventory.repository.OrderMaterialRepository;

@Service
public class OrderMaterialService {

    private final OrderMaterialRepository orderMaterialRepository;
    private final MaterialService materialService;

    public OrderMaterialService(OrderMaterialRepository orderMaterialRepository, MaterialService materialService) {
        this.orderMaterialRepository = orderMaterialRepository;
        this.materialService = materialService;
    }

    public OrderMaterial createOrderMaterial(String code, String description, double quantity, Order newOrder) {
        Material materialEntity = materialService.getOrCreateMaterial(code, description);
        OrderMaterial orderMaterial = new OrderMaterial();
        orderMaterial.setMaterial(materialEntity);
        orderMaterial.setQuantity(quantity);
        orderMaterial.setOrder(newOrder);
        return orderMaterialRepository.save(orderMaterial);
    }
}
