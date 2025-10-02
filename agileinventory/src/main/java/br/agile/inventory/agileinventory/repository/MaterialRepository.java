package br.agile.inventory.agileinventory.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.agile.inventory.agileinventory.model.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    @Query("SELECT m FROM Material m JOIN FETCH m.productOrder")
    List<Material> findAllWithProductionOrder();
}
