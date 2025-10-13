package br.agile.inventory.agileinventory.dto;

public class OrderMaterialRequest {
    private Long id;
    private String code;
    private String description;
    private double quantity;
    private Long materialId;
    
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long material_id) {
        this.materialId = material_id;
    }
}
