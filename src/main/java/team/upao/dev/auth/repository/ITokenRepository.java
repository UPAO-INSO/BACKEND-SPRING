package team.upao.dev.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.upao.dev.auth.model.TokenModel;
import team.upao.dev.users.model.UserModel;

import java.util.List;
import java.util.Optional;

public interface ITokenRepository extends JpaRepository<TokenModel, Long> {
    List<TokenModel> findAllByUser(UserModel user);

    Optional<TokenModel> findByToken(String jwt);
}
