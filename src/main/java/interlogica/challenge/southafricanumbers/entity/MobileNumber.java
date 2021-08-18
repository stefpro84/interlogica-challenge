package interlogica.challenge.southafricanumbers.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL) 
@Entity
public class MobileNumber {
	
	@Id	
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonProperty(value = "id")
	private Long externalId;
	private String number;
	private String sourceNumber;
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private EnumStatusMobileNumber status;
	
	private Boolean attemptedCorrection;
	private String correctionDetails;
	
	@JsonIgnore	
	@ManyToOne(fetch = FetchType.LAZY)
	private CsvFile csvFile;

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}



	public EnumStatusMobileNumber getStatus() {
		return status;
	}


	public void setStatus(EnumStatusMobileNumber status) {
		this.status = status;
	}


	public String getCorrectionDetails() {
		return correctionDetails;
	}


	public void setCorrectionDetails(String correctionDetails) {
		this.correctionDetails = correctionDetails;
	}


	public CsvFile getCsvFile() {
		return csvFile;
	}


	public void setCsvFile(CsvFile csvFile) {
		this.csvFile = csvFile;
	}


	public String getSourceNumber() {
		return sourceNumber;
	}


	public void setSourceNumber(String sourceNumber) {
		this.sourceNumber = sourceNumber;
	}


	public Boolean getAttemptedCorrection() {
		return attemptedCorrection;
	}


	public void setAttemptedCorrection(Boolean attemptedCorrection) {
		this.attemptedCorrection = attemptedCorrection;
	}
	
	
	public Long getExternalId() {
		return externalId;
	}


	public void setExternalId(Long externalId) {
		this.externalId = externalId;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MobileNumber [id=").append(id).append(", externalId=").append(externalId).append(", number=")
				.append(number).append(", sourceNumber=").append(sourceNumber).append(", status=").append(status)
				.append(", attemptedCorrection=").append(attemptedCorrection).append(", correctionDetails=")
				.append(correctionDetails).append("]");
		return builder.toString();
	}

	
	
}
