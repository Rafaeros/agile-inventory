package br.agile.inventory.agileinventory.model;

import java.util.List;

public class OrderForm {
    private long orderId;
    
    private String orderNumber;
    private String orderCode;
    private int orderQuantity;
    private List<MaterialItem> materials;
    
    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
    // Getters e Setters
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public List<MaterialItem> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MaterialItem> materials) {
        this.materials = materials;
    }

    // Classe interna para representar os materiais
    public static class MaterialItem {
        private String code;
        private String description;
        private int quantity;

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
    }
}
