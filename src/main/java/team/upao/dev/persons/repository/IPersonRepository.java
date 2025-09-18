package team.upao.dev.persons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.upao.dev.persons.model.PersonModel;

@Repository
public interface IPersonRepository extends JpaRepository<PersonModel, Long> {
}
