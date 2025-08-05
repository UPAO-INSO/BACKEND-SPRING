package upao.inso.dclassic.clients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upao.inso.dclassic.clients.model.ClientModel;

import java.util.Optional;

@Repository
public interface IClientRepository extends JpaRepository<ClientModel, Long> {
    Optional<ClientModel> findByEmail(String email);
    Optional<ClientModel> findByPhone(String phone);
    Optional<ClientModel> findByDocumentNumber(String document);
}
