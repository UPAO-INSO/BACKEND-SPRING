package team.upao.dev.products.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import team.upao.dev.products.model.ProductModel;
import team.upao.dev.products.model.ProductTypeModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<ProductModel, Long> {
    Optional<ProductModel> findByNameIgnoreCase(String name);
    Optional<List<ProductModel>> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
    @NonNull
    Page<ProductModel> findAll(@NonNull Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update ProductModel p set p.name=:name where p.id=:id")
    @NonNull
    void updateNameById(@Param(value = "id") Long id, @Param(value = "name") String name);

    @Modifying(clearAutomatically = true)
    @Query("update ProductModel p set p.price=:price where p.id=:id")
    @NonNull
    void updatePriceById(@Param(value = "id") Long id, @Param(value = "price") Double price);

    @Modifying(clearAutomatically = true)
    @Query("update ProductModel p set p.description=:description where p.id=:id")
    @NonNull
    void updateDescriptionById(@Param(value = "id") Long id, @Param(value = "description") String description);

    @Modifying(clearAutomatically = true)
    @Query("update ProductModel p set p.productType=:productType where p.id=:id")
    @NonNull
    void updateProductTypeById(@Param(value = "id") Long id, @Param(value = "productType") ProductTypeModel productType);

    @Modifying(clearAutomatically = true)
    @Query("update ProductModel p set p.active=:active where p.id=:id")
    @NonNull
    void updateActiveById(@Param(value = "id") Long id, @Param(value = "active") Boolean active);

    @Modifying(clearAutomatically = true)
    @Query("update ProductModel p set p.available=:available where p.id=:id")
    @NonNull
    void updateAvailableById(@Param(value = "id") Long id, @Param(value = "available") Boolean available);
}
