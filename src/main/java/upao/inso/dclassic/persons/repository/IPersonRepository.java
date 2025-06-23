package upao.inso.dclassic.persons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upao.inso.dclassic.persons.model.PersonModel;

@Repository
public interface IPersonRepository extends JpaRepository<PersonModel, Long> {
}
