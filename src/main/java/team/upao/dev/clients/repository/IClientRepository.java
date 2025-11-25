package team.upao.dev.clients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.upao.dev.clients.model.ClientModel;

import java.util.Optional;

@Repository
public interface IClientRepository extends JpaRepository<ClientModel, Long> {
    Optional<ClientModel> findByEmail(String email);
    Optional<ClientModel> findByPhone(String phone);
    Optional<ClientModel> findByDocumentNumberContaining(String documentNumber);
}
