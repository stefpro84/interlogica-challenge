package interlogica.challenge.southafricanumbers.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) 
@Entity
public class CsvFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User owner;
	
	@Column
	private String filename;
		
	@OneToMany(fetch = FetchType.LAZY, mappedBy= "csvFile", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private Set<MobileNumber> numbers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Set<MobileNumber> getNumbers() {
		return numbers;
	}

	public void setNumbers(Set<MobileNumber> numbers) {
		this.numbers = numbers;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CsvFile [id=").append(id).append(", filename=").append(filename).append("]");
		return builder.toString();
	}
	
	

}
