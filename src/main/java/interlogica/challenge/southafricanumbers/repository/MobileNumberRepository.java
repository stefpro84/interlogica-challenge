package interlogica.challenge.southafricanumbers.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import interlogica.challenge.southafricanumbers.entity.CsvFile;
import interlogica.challenge.southafricanumbers.entity.MobileNumber;
import interlogica.challenge.southafricanumbers.entity.User;

@Repository
public interface MobileNumberRepository extends CrudRepository<MobileNumber, Long> {
		
	public List<MobileNumber> findByCsvFileOwner(User user);
		
	public Optional<MobileNumber> findByCsvFileOwnerAndId(User user, Long id);
	
	public List<MobileNumber> findByCsvFile(CsvFile csvFile);
}
