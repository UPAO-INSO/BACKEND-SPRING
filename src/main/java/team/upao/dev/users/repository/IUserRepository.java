package team.upao.dev.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.upao.dev.users.model.UserModel;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByEmailOrUsername(String email, String username);

    @Modifying()
    @Query("update UserModel u set u.username=:username where u.id=:id")
    void updateUsernameById(@Param(value = "id") Long id, @Param(value = "username") String username);

    @Modifying()
    @Query("update UserModel u set u.email=:email where u.id=:id")
    void updateEmailById(@Param(value = "id") Long id, @Param(value = "email") String email);

    @Query("SELECT p.name, p.lastname " +
            "FROM UserModel u " +
            "LEFT JOIN EmployeeModel e ON e.user.id = u.id " +
            "LEFT JOIN PersonModel p ON p.id = e.id " +
            "WHERE u.username = :username")
    Optional<String> findUserWithFullNameByUsername(@Param("username") String username);
}
