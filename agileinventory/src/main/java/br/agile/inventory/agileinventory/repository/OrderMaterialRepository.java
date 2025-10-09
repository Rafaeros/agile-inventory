package br.agile.inventory.agileinventory.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import br.agile.inventory.agileinventory.model.OrderMaterial;


@Repository
public interface OrderMaterialRepository extends JpaRepository<OrderMaterial, Long> {}
