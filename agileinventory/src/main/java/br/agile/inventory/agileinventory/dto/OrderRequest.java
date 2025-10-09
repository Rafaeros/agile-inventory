package br.agile.inventory.agileinventory.dto;

import java.util.List;

public class OrderRequest {
    private Long orderId;
    private String orderNumber;
    private String code;
    private String description;
    private int quantity;
    private List<OrderMaterialRequest> materials;
    
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<OrderMaterialRequest> getMaterials() {
        return materials;
    }
    public void setMaterials(List<OrderMaterialRequest> materials) {
        this.materials = materials;
    }

    
}
