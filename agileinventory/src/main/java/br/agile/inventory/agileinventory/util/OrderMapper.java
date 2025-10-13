package br.agile.inventory.agileinventory.util;

import java.util.List;
import java.util.stream.Collectors;

import br.agile.inventory.agileinventory.dto.OrderMaterialRequest;
import br.agile.inventory.agileinventory.dto.OrderRequest;
import br.agile.inventory.agileinventory.model.Order;
import br.agile.inventory.agileinventory.model.OrderMaterial;

public class OrderMapper {

    public static OrderRequest toDto(Order order) {
        OrderRequest dto = new OrderRequest();
        dto.setId(order.getId());
        dto.setOrderId(order.getOrderId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setQuantity(order.getQuantity());
        dto.setCode(order.getProduct().getCode());
        dto.setDescription(order.getProduct().getDescription());

        List<OrderMaterialRequest> materials = order.getMaterials()
                .stream()
                .map(OrderMapper::toMaterialDto)
                .collect(Collectors.toList());

        dto.setMaterials(materials);
        return dto;
    }

    private static OrderMaterialRequest toMaterialDto(OrderMaterial material) {
        OrderMaterialRequest dto = new OrderMaterialRequest();
        dto.setId(material.getId());
        dto.setCode(material.getMaterial().getCode());
        dto.setDescription(material.getMaterial().getDescription());
        dto.setQuantity(material.getQuantity());
        dto.setMaterialId(material.getMaterial().getId());
        return dto;
    }
}
