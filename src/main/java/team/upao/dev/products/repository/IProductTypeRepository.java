package team.upao.dev.products.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import team.upao.dev.products.model.ProductTypeModel;

import java.util.Optional;

@Repository
public interface IProductTypeRepository extends JpaRepository<ProductTypeModel, Long> {
    @NonNull
    Page<ProductTypeModel> findAll(@NonNull Pageable pageable);
    Optional<ProductTypeModel> findByName(String nameType);
    boolean existsByName(String nameType);

    @Modifying(clearAutomatically = true)
    @Query("update ProductTypeModel pt set pt.name=:name where pt.id=:id")
    @NonNull
    void updateNameById(@Param(value = "id") Long id, @Param(value = "name") String name);
}
