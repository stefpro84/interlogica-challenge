package interlogica.challenge.southafricanumbers.repository;

import org.springframework.stereotype.Repository;

import interlogica.challenge.southafricanumbers.entity.User;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	
}
