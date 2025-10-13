package br.agile.inventory.agileinventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.agile.inventory.agileinventory.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    Optional<Order> findByOrderId(Long orderId);

    List<Order> findFirst20By();

    @Query("SELECT o FROM Order o JOIN o.product p WHERE p.code LIKE %:code%")
    List<Order> findOrdersByProductCode(@Param("code") String code);

    @Query("SELECT DISTINCT o FROM Order o " +
       "JOIN FETCH o.product p " +
       "LEFT JOIN FETCH o.materials m " +
       "WHERE p.code LIKE %:code%")
    List<Order> findOrdersByProductCodeWithMaterials(@Param("code") String code);

    @Query("SELECT DISTINCT o FROM Order o " +
              "LEFT JOIN FETCH o.product " +
              "LEFT JOIN FETCH o.materials m " +
              "WHERE o.id = :id")
    Optional<Order> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.product " +
           "LEFT JOIN FETCH o.materials m")
    List<Order> findAllWithDetails();
}
