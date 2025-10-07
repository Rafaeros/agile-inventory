package br.agile.inventory.agileinventory.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

import br.agile.inventory.agileinventory.model.ProductionOrder;

public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, Long> {
    @Query("SELECT DISTINCT po FROM ProductionOrder po LEFT JOIN FETCH po.materials")
    List<ProductionOrder> findAllWithMaterials();

    boolean existsByOrderId(long orderId);
    Optional<ProductionOrder> findByOrderId(long orderId);
}