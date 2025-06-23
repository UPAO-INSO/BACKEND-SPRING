package upao.inso.dclassic.clients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upao.inso.dclassic.clients.model.ClientModel;

@Repository
public interface IClientRepository extends JpaRepository<ClientModel, Long> {
}
