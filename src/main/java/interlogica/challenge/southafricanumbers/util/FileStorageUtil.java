package interlogica.challenge.southafricanumbers.util;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.h2.util.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import interlogica.challenge.southafricanumbers.entity.CsvFile;
import interlogica.challenge.southafricanumbers.entity.MobileNumber;


public class FileStorageUtil {
	
	public static final long CSV_FILE_MAX_SIZE = 131072L;	
	public final static String INPUT_CSV_ID = "id";
	public final static String INPUT_CSV_NUMBER = "number";	
	public final static String OUTPUT_CSV_ID= "id";
	public final static String OUTPUT_CSV_NUMBER = "number";
	public final static String OUTPUT_CSV_IS_VALID = "isValid";
	public final static String OUTPUT_CSV_CORRECTION = "correction";
	public final static String OUTPUT_CSV_SOURCE = "source";		
	public final static String OUTPUT_CSV_DETAILS = "details";
	
	public final static String[] OUTPUT_CSV_HEADERS = {OUTPUT_CSV_ID,
													   OUTPUT_CSV_NUMBER,													   
													   OUTPUT_CSV_SOURCE,
													   OUTPUT_CSV_IS_VALID,
													   OUTPUT_CSV_CORRECTION,								   
													   OUTPUT_CSV_DETAILS};		
	
	/**
	 * Store a csv in a temporary file and returns a URL which points to the local file.
	 * @param csvFile
	 * @return
	 * @throws IOException
	 */
	public static byte[] getOutputCsv (CsvFile csvFile) throws IOException {
		
		byte[] fileContent;
		Path tempFile = Files.createTempFile(null, ".csv");
		
		// write data on temporary csv file
		try (FileWriter fileWriter = new FileWriter(tempFile.toFile());
			 CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.builder().setHeader(OUTPUT_CSV_HEADERS).build()))
		{
			for (MobileNumber mobileNumber: csvFile.getNumbers()) {
				csvPrinter.printRecord(
						mobileNumber.getExternalId(),
						mobileNumber.getNumber(),
						mobileNumber.getSourceNumber(),
						mobileNumber.getStatus(),
						mobileNumber.getAttemptedCorrection(),
						mobileNumber.getCorrectionDetails()
				);
			}
		}
		
		// read csv file content
		try (FileInputStream fis = new FileInputStream(tempFile.toString())) {
			fileContent = IOUtils.readBytesAndClose(fis, -1);
		}
		
		// delete temporary file
		silentDelete(tempFile);
		
		return fileContent;
	}
	
	/**
	 * Quietly delete the csv file at the given input path.
	 * @param path
	 */
	public static void silentDelete (Path path) {
		// silent file delete
		try {
			Files.deleteIfExists(path);
		}
		catch (Exception ex) {
			
		}
	}

}
