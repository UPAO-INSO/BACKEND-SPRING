package team.upao.dev.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.upao.dev.inventory.model.InventoryModel;
import team.upao.dev.inventory.enums.InventoryType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface IInventoryRepository extends JpaRepository<InventoryModel, Long> {
    //BLOQUE DE BÚSQUEDA Y FILTROS
    // Buscar por nombre (ignora mayusculas o minusculas)
    Optional<InventoryModel> findByNameIgnoreCase(String name);
    // Buscar por una cadena específica dentro del nombre (ej: todos aquellos que contengan la palabra "aceite")
    Optional<List<InventoryModel>> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
    
    Page<InventoryModel> findAll(Pageable pageable);

    Page<InventoryModel> findAllByType(Pageable pageable, InventoryType type);
    
    // Filtrado avanzado: Para múltiples tipos
    Page<InventoryModel> findAllByTypeIn(Pageable pageable, List<InventoryType> types);

    
    @Query("SELECT i FROM InventoryModel i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<InventoryModel> searchByName(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT i.quantity FROM InventoryModel i WHERE i.id = :id")
    Optional<BigDecimal> getQuantityById(@Param("id") Long id);

    // Buscar por tipo y nombre
    @Query("SELECT i FROM InventoryModel i WHERE i.type = :type AND i.name LIKE %:name%")
    Page<InventoryModel> findByTypeAndName(
        @Param("type") InventoryType type,
        @Param("name") String name,
        Pageable pageable
    );

    // NUEVOS: Para validación de stock
    @Query("SELECT i FROM InventoryModel i WHERE i.quantity <= 0 AND i.type = 'INGREDIENT'")
    Page<InventoryModel> findOutOfStockItems(Pageable pageable);

    //BLOQUE DE MODIFICACIONES
    @Modifying(clearAutomatically = true)
    @Query("update InventoryModel i set i.name=:name where i.id=:id")
    void updateNameById(@Param(value = "id") Long id, @Param(value = "name") String name);

    // Método para actualizar la cantidad de un ítem en el inventario
    @Modifying(clearAutomatically = true)
    @Query("update InventoryModel i set i.quantity = :quantity where i.id = :id")
    void updateQuantityById(@Param("id") Long id, @Param("quantity") Double quantity);

    @Modifying(clearAutomatically = true)
    @Query("update InventoryModel i set i.type = :type where i.id = :id")
    void updateTypeById(@Param("id") Long id, @Param("type") InventoryType type);

    @Modifying(clearAutomatically = true)
    @Query("update InventoryModel i set i.unitOfMeasure = :unitOfMeasure where i.id = :id")
    void updateUnitOfMeasureById(@Param("id") Long id, @Param("unitOfMeasure") String unitOfMeasure);

}   