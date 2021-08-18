package interlogica.challenge.southafricanumbers.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import interlogica.challenge.southafricanumbers.entity.EnumRole;
import interlogica.challenge.southafricanumbers.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository <Role, Integer>{

	Optional<Role> findByName(EnumRole name);
	
}
