package br.agile.inventory.agileinventory.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;

@Entity
public class ProductionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private long orderId;

    @Column(unique = true)
    private String orderNumber;

    private String description;
    private int quantity;

    @OneToMany(mappedBy = "productOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Material> materials;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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
    public List<Material> getMaterials() {
        return materials;
    }
    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

   
   
}
