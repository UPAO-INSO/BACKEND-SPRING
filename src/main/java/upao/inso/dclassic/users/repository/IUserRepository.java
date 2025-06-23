package upao.inso.dclassic.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import upao.inso.dclassic.users.model.UserModel;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findByEmail(String email);

    @Modifying()
    @Query("update UserModel u set u.username=:username where u.id=:id")
    void updateUser(@Param(value = "id") Long id, @Param(value = "username") String username);
}
