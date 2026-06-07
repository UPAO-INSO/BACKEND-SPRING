package team.upao.dev.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.upao.dev.menu.model.MenuDiarioItemModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IMenuDiarioItemRepository extends JpaRepository<MenuDiarioItemModel, Long> {

    @Query("SELECT m FROM MenuDiarioItemModel m JOIN FETCH m.product p LEFT JOIN FETCH p.productType WHERE m.product.id = :productId AND m.date = :date")
    Optional<MenuDiarioItemModel> findByProductIdAndDate(@Param("productId") Long productId, @Param("date") LocalDate date);

    @Query("SELECT m FROM MenuDiarioItemModel m JOIN FETCH m.product p LEFT JOIN FETCH p.productType WHERE m.date = :date ORDER BY p.id")
    List<MenuDiarioItemModel> findAllByDateOrderByProductId(@Param("date") LocalDate date);
}
