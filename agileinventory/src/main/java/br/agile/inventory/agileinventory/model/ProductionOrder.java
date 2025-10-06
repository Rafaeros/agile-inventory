package br.agile.inventory.agileinventory.model;

import java.util.List;
import java.util.Date;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class ProductionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String orderNumber;
    private String description;
    private int quantity;

    @OneToMany(mappedBy = "productOrder")
    private List<Material> materials;

    private Date createdAt = new Date();
    private Date updatedAt = new Date();

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
   
}
