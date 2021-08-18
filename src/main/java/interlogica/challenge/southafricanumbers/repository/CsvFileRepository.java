package interlogica.challenge.southafricanumbers.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import interlogica.challenge.southafricanumbers.entity.CsvFile;
import interlogica.challenge.southafricanumbers.entity.User;

@Repository
public interface CsvFileRepository extends CrudRepository<CsvFile, Long>{
	
	List<CsvFile> findByOwner(User user);
	
	Optional<CsvFile> findByIdAndOwner(Long id, User user);
	
	Optional<CsvFile> findByFilenameAndOwner(String filename, User user);
	
	void deleteByFilenameAndOwner(String filename, User user);
	
}
