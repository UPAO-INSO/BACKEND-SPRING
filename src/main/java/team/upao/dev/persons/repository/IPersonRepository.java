package team.upao.dev.persons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.upao.dev.persons.model.PersonModel;

import java.util.Optional;

@Repository
public interface IPersonRepository extends JpaRepository<PersonModel, Long> {
    Optional<PersonModel> findByNameAndLastnameContaining(String name, String lastname);
}
