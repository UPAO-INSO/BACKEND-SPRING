package team.upao.dev.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.upao.dev.inventory.model.ProductInventoryModel;
import java.util.List;
import java.util.Optional;

@Repository
public interface IProductInventoryRepository extends JpaRepository<ProductInventoryModel, Long> {
  
List<ProductInventoryModel> findByProductId(Long productId);
//Se supone q hace un join con la tabla product para conseguir su nombre
Optional<ProductInventoryModel> findByProduct_NameIgnoreCase(String name);

@Query("SELECT CASE WHEN COUNT(pi) > 0 THEN true ELSE false END FROM ProductInventoryModel pi WHERE pi.product.id = :productId")
boolean hasRecipe(@Param("productId") Long productId);

//Modifica la cantidad de un item asociado a un producto
@Modifying
@Query("UPDATE ProductInventoryModel p SET p.quantity = :quantity WHERE p.product.id = :productId AND p.inventory.id = :inventoryId")
void updateQuantityByProductAndInventory(
    @Param("productId") Long productId, 
    @Param("inventoryId") Long inventoryId, 
    @Param("quantity") java.math.BigDecimal quantity
);

// Modificar unidad de medida en receta
@Modifying
@Query("UPDATE ProductInventoryModel p SET p.unitOfMeasure = :unitOfMeasure WHERE p.product.id = :productId AND p.inventory.id = :inventoryId")
void updateUnitOfMeasureByProductAndInventory(
    @Param("productId") Long productId, 
    @Param("inventoryId") Long inventoryId, 
    @Param("unitOfMeasure") String unitOfMeasure
);
    
// NUEVOS: Para validación y deducción de stock en órdenes
@Query("SELECT pi FROM ProductInventoryModel pi WHERE pi.product.id = :productId AND pi.inventory.type = 'INGREDIENT'")
List<ProductInventoryModel> findIngredientsForDeduction(@Param("productId") Long productId);

@Query("SELECT DISTINCT p.id FROM ProductModel p WHERE NOT EXISTS (SELECT 1 FROM ProductInventoryModel pi WHERE pi.product.id = p.id) OR p.id IN (SELECT pi.product.id FROM ProductInventoryModel pi JOIN InventoryModel i ON pi.inventory.id = i.id WHERE i.quantity <= 0)")
List<Long> findUnsellableProducts();
}    