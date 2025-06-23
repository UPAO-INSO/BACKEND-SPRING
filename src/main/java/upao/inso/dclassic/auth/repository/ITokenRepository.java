package upao.inso.dclassic.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upao.inso.dclassic.auth.model.TokenModel;
import upao.inso.dclassic.users.model.UserModel;

import java.util.List;
import java.util.Optional;

public interface ITokenRepository extends JpaRepository<TokenModel, Long> {
    List<TokenModel> findAllByUser(UserModel user);

    Optional<TokenModel> findByToken(String jwt);
}
