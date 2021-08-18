package interlogica.challenge.southafricanumbers.service;

import static interlogica.challenge.southafricanumbers.util.FileStorageUtil.CSV_FILE_MAX_SIZE;
import static interlogica.challenge.southafricanumbers.util.FileStorageUtil.INPUT_CSV_ID;
import static interlogica.challenge.southafricanumbers.util.FileStorageUtil.INPUT_CSV_NUMBER;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import interlogica.challenge.southafricanumbers.entity.CsvFile;
import interlogica.challenge.southafricanumbers.entity.MobileNumber;
import interlogica.challenge.southafricanumbers.entity.User;
import interlogica.challenge.southafricanumbers.repository.CsvFileRepository;
import interlogica.challenge.southafricanumbers.repository.MobileNumberRepository;
import interlogica.challenge.southafricanumbers.repository.UserRepository;
import interlogica.challenge.southafricanumbers.util.FileStorageUtil;
import interlogica.challenge.southafricanumbers.util.MobileNumberUtil;


@Service
@Transactional(rollbackFor= {UsernameNotFoundException.class, IOException.class})
public class FileCsvService {
	
	private final static Logger logger = LoggerFactory.getLogger(FileCsvService.class);

	@Autowired
	MobileNumberRepository mobileNumberRepository;	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CsvFileRepository csvFileRepository;
	
	/**
	 * Process and store a csv file given in input.
	 * @param username
	 * @param file
	 * @return
	 * @throws UsernameNotFoundException
	 * @throws IOException
	 */
	public CsvFile storeCsvFile(String username, MultipartFile file) throws UsernameNotFoundException, IOException {	
		
		User user = userRepository.findByUsername(username).get();
		String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		logger.info("processing file {}...", fileName);
		
		// validate input csv			
		Set<MobileNumber> mobileNumbers = validateMultipartFile(file);
		
		
		// save/replace the csv file
		CsvFile csvFile; 		
		Optional<CsvFile> checkIfExists = csvFileRepository.findByFilenameAndOwner(fileName, user);		
		if (checkIfExists.isPresent()) {
			logger.debug("file '{}' already exists, updating...");
			csvFile = checkIfExists.get();			
			csvFileRepository.delete(csvFile);				
		}
		
		csvFile = new CsvFile();			
		csvFile.setOwner(user);
		csvFile.setFilename(fileName);						
		csvFile = csvFileRepository.save(csvFile);
		logger.debug("csv file stored with id: {}", csvFile.getId());
		
		// save/update mobile numbers
		for (MobileNumber mobileNumber: mobileNumbers) {
			mobileNumber.setCsvFile(csvFile);
		}
		Set<MobileNumber> fileNumbers = new HashSet<>();		
		Iterable<MobileNumber> numbers = mobileNumberRepository.saveAll(mobileNumbers);
		numbers.forEach(fileNumbers::add);
		csvFile.setNumbers(fileNumbers);
		logger.debug("saved csv file {} with its {} numbers", csvFile.getFilename(), fileNumbers.size());
		
		return csvFile;
	}
	
	/**
	 * Retrieve all CsvFiles for a specific user.
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 * @throws IOException
	 */
	public List<CsvFile> retrieveCsvFiles (String username) throws UsernameNotFoundException, IOException {
		
		logger.info("retrieving all csv files for user: {}", username);
		
		User user = userRepository.findByUsername(username).get();
		List<CsvFile> csvFiles = csvFileRepository.findByOwner(user);		
		logger.debug("found {} csv files", csvFiles.size());		
		
		return csvFiles;		
	}
	
	
	/**
	 * Retrieve a specific CsvFile for a user.
	 * @param username
	 * @param csvFileId
	 * @return
	 * @throws NoSuchElementException
	 */
	public CsvFile retrieveCsvFile (String username, Long csvFileId) throws NoSuchElementException, Exception {
		
		logger.info("getting csv file {} for user {}...", csvFileId, username);
		
		User user = userRepository.findByUsername(username).get();
		Optional<CsvFile> optional = csvFileRepository.findByIdAndOwner(csvFileId, user);
		
		if (optional.isPresent()) {
			logger.debug("csv file found: {}", optional.get());
		}
		else {
			logger.debug("csv file not found");
		}		
		
		return optional.get();
	}
	
	/**
	 * Process a CsvFile 
	 * @param csvFile
	 * @param csvFilePath
	 * @return
	 * @throws IOException
	 */
	public Set<MobileNumber> processCsvFile(Path csvFilePath) throws IOException {
		
		logger.debug("processing csv file ...");
		
		// check file format				
		try (FileReader reader = new FileReader(csvFilePath.toString(), StandardCharsets.UTF_8)){
			
			Set<Long> ids = new HashSet<>();
			Set<MobileNumber> mobileNumbers = new HashSet<>();
						
			Iterator<CSVRecord> records = CSVFormat.DEFAULT.builder()
					.setHeader(INPUT_CSV_ID, INPUT_CSV_NUMBER)
	                .setSkipHeaderRecord(true)
	                .build()
	                .parse(reader)
	                .iterator();
			
			while (records.hasNext()) {
				var record = records.next();
				Long id = Long.valueOf(record.get(INPUT_CSV_ID));
				if (ids.contains(id)) {
					logger.error("Duplicated number id " + id);
					throw new IOException("Duplicated number id " + id);
				}
				var mobileNumber = MobileNumberUtil.processMobileNumber(record.get(INPUT_CSV_NUMBER));
				mobileNumber.setId(id);
				mobileNumbers.add(mobileNumber);
			}
			
			logger.debug("file processed successfully");
			
			return mobileNumbers;
		}				
	}
	
	/**
	 * Validate the CsvFile given inside the multipart form input request.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private Set<MobileNumber> validateMultipartFile(MultipartFile file) throws IOException {
		
		Set<MobileNumber> mobileNumbers = new HashSet<MobileNumber>();
		
		// check empty file
		if (file.isEmpty()) {
			throw new IOException("The provided file is empty");
		}
		
		// check file size
		if (file.getSize() >= CSV_FILE_MAX_SIZE) {
			throw new IOException("The provided file exceeds max size of " + CSV_FILE_MAX_SIZE + " bytes");
		}
		
		// check file format
		Path tmp = Files.createTempFile(null, ".csv");
		try (FileOutputStream fos = new FileOutputStream(tmp.toFile())){
			fos.write(file.getBytes());
		}
		try (FileReader reader = new FileReader(tmp.toFile(), StandardCharsets.UTF_8)){
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder()
	                .setHeader(INPUT_CSV_ID, INPUT_CSV_NUMBER)
	                .setSkipHeaderRecord(true)
	                .build()
	                .parse(reader);
			
			Set<Long> idPool = new HashSet<>();
			// iterate over records
			for(CSVRecord record: records) {
				String idValue = record.get(INPUT_CSV_ID);
				if (StringUtils.isBlank(idValue)) {
					new IOException("Missing id on record: " + record.getRecordNumber());
				}
				else if (!StringUtils.isNumeric(INPUT_CSV_ID)) {
					new IOException("Invalid id value \"" + idValue + "\" specified for record: " + record.getRecordNumber());
				}
				else if (record.isSet(INPUT_CSV_NUMBER)) {
					new IOException("Missing number value on record: " + record.getRecordNumber());
				}
				Long id = Long.valueOf(idValue);
				if (idPool.contains(id)) {
					logger.error("Duplicated number id " + id);
					throw new IOException("Duplicated number id " + id);
				}
				else {
					idPool.add(id);
				}
				MobileNumber mobileNumber = MobileNumberUtil.processMobileNumber(record.get(INPUT_CSV_NUMBER));
				mobileNumber.setExternalId(id);				
				mobileNumbers.add(mobileNumber);
			}
			
		}
		
		// delete temporary file
		FileStorageUtil.silentDelete(tmp);
		
		return mobileNumbers;
	}
	
	
	
	
	
}
