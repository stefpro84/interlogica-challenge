package interlogica.challenge.southafricanumbers.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import interlogica.challenge.southafricanumbers.entity.CsvFile;
import interlogica.challenge.southafricanumbers.rest.model.BasicResponse;
import interlogica.challenge.southafricanumbers.rest.model.CsvFileLight;
import interlogica.challenge.southafricanumbers.rest.model.SubmitMobileNumberRequest;
import interlogica.challenge.southafricanumbers.security.jwt.JwtUtils;
import interlogica.challenge.southafricanumbers.security.services.UserDetailsServiceImpl;
import interlogica.challenge.southafricanumbers.service.FileCsvService;
import interlogica.challenge.southafricanumbers.util.FileStorageUtil;
import interlogica.challenge.southafricanumbers.util.MobileNumberUtil;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/app")
public class AppApi {
	
	private final static Logger logger = LoggerFactory.getLogger(AppApi.class);
	private final static String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error!";
	
	@Autowired
	private FileCsvService fileCsvService;
	
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@PostMapping(
			value="/csvFile",
			consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE
	)	
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?>  uploadCsvFile(Principal principal, @RequestParam("file") MultipartFile file) {
		
		String username = principal.getName();
		logger.info("csv file upload request by user {}", username);
				
		HttpStatus httpStatus;
		Object body;
		
		try {
			body = fileCsvService.storeCsvFile(username, file);
			httpStatus = HttpStatus.CREATED;			
		}
		catch (UsernameNotFoundException unfe) {
			logger.error("Error: ", unfe.getMessage());
			httpStatus = HttpStatus.FORBIDDEN;
			body = new BasicResponse(unfe.getMessage());
		}
		catch (IOException ioe) {
			logger.error("Error: ", ioe.getMessage());
			httpStatus = HttpStatus.BAD_REQUEST;
			body = new BasicResponse(ioe.getMessage());
		}
		catch (Exception ex) {
			logger.error("Error: ", ex);
			httpStatus = HttpStatus.BAD_REQUEST;
			body = new BasicResponse(INTERNAL_SERVER_ERROR_MESSAGE);
		}
		return ResponseEntity.status(httpStatus).body(body);
	}
	
	
	@GetMapping(
			value="/csvFile",
			produces=MediaType.APPLICATION_JSON_VALUE
	)	
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getCsvFiles (Principal principal) {
		
		String username = principal.getName();
		logger.info("user {} has made a csf file upload request", username);
		
		HttpStatus httpStatus;
		Object body;
		
		try {					
			httpStatus = HttpStatus.CREATED;
			body = fileCsvService.retrieveCsvFiles(username).stream()
														    .map(csvFile -> new CsvFileLight(csvFile.getId(), csvFile.getFilename()))
															.collect(Collectors.toList());
		}
		catch (UsernameNotFoundException unfe) {
			httpStatus = HttpStatus.FORBIDDEN;
			body = new BasicResponse(unfe.getMessage());
		}
		catch (Exception ex) {
			logger.error("Error: ", ex);
			httpStatus = HttpStatus.BAD_REQUEST;
			body = new BasicResponse(INTERNAL_SERVER_ERROR_MESSAGE);
		}
		return ResponseEntity.status(httpStatus).body(body);
	}
	
	
	@GetMapping(
			value="/csvFile/{id}",
			produces=MediaType.APPLICATION_JSON_VALUE
	)	
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getCsvFile (Principal principal, @PathVariable Long id) {
		
		String username = principal.getName();
		logger.info("user {} has made a csf file upload request", username);
		
		HttpStatus httpStatus;
		Object body;
		
		try {		
			body = fileCsvService.retrieveCsvFile(username, id);
			httpStatus = HttpStatus.CREATED;			
		}
		catch (UsernameNotFoundException unfe) {
			httpStatus = HttpStatus.FORBIDDEN;
			body = new BasicResponse(unfe.getMessage());
		}
		catch (NoSuchElementException nsee) {
			httpStatus = HttpStatus.NOT_FOUND;
			body = new BasicResponse("Csv file not found");
		}
		catch (Exception ex) {
			logger.error("Error: ", ex);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			body = new BasicResponse(INTERNAL_SERVER_ERROR_MESSAGE);
		}
		return ResponseEntity.status(httpStatus).body(body);
	}
	
	
	@GetMapping(
			value="/csvFile/download/{id}",
			produces= {MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
	)	
	public ResponseEntity<?> downloadCsvFile (@PathVariable Long id, @RequestParam(required=true) String jwt) {
				
		ResponseEntity<?> response;
		
		try {
			String username = jwtUtils.getUserNameFromJwtToken(jwt);
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				userDetailsService.loadUserByUsername(username);
			}
			logger.info("csv file {} download request from user {}...", id, username);
					
			CsvFile csvFile = fileCsvService.retrieveCsvFile(username, id);
			byte[] body = FileStorageUtil.getOutputCsv(csvFile);
			response = ResponseEntity.status(HttpStatus.OK)
								     .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
									 .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(body.length))
									 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + csvFile.getFilename() + "\"")
									 .body(body);
		}
		catch (UsernameNotFoundException unfe) {
			response = ResponseEntity.status(HttpStatus.FORBIDDEN)
									 .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
									 .body(new BasicResponse("Forbidden"));
		}
		catch (NoSuchElementException nsee) {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND)
									 .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
									 .body(new BasicResponse("Csv file not found"));			
		}
		catch (Exception ex) {
			logger.error("Error: ", ex);
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BasicResponse(INTERNAL_SERVER_ERROR_MESSAGE));
		}
		
		return response;
	}
	
	
	@PostMapping(
			value="/mobileNumber",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE
	)	
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> submitmobileNumber (Principal principal, @Valid @RequestBody SubmitMobileNumberRequest stnr) {
				
		logger.info("user {} has made a mobile number check request", principal.getName());
		
		HttpStatus httpStatus;
		Object body;
		
		try {
			body = MobileNumberUtil.processMobileNumber(stnr.getmobileNumber());
			httpStatus = HttpStatus.OK;
		}
		catch (Exception ex) {
			logger.error("Error: ", ex);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			body = new BasicResponse(INTERNAL_SERVER_ERROR_MESSAGE);
		}
		
		return ResponseEntity.status(httpStatus).body(body);
	}

}
