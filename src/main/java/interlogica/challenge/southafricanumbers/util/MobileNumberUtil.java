package interlogica.challenge.southafricanumbers.util;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import interlogica.challenge.southafricanumbers.entity.EnumStatusMobileNumber;
import interlogica.challenge.southafricanumbers.entity.MobileNumber;


public class MobileNumberUtil {

	private final static Logger logger = LoggerFactory.getLogger(MobileNumberUtil.class);

	// "27831234567"	
	public final static int DEFAULT_NUMBER_LENGTH = 11;
	private final static Pattern MOBILE_NUMBER_FORMAT_REGEX = Pattern.compile("^[0-9]{11}$");		
	
	
	/**
	 * Process a given mobile number.
	 * @param number
	 * @return
	 */
	public static MobileNumber processMobileNumber (String number) {
		
		MobileNumber mobileNumber;			
		
		// base check
		if (MOBILE_NUMBER_FORMAT_REGEX.matcher(number).matches()) {
			mobileNumber = new MobileNumber();
			mobileNumber.setSourceNumber(number);
			mobileNumber.setNumber(number);
			mobileNumber.setStatus(EnumStatusMobileNumber.VALID);
			mobileNumber.setAttemptedCorrection(false);
		}
		// correction attempt
		else {
			mobileNumber = mobileNumberCorrectionAttempt(number);
		}
		
		return mobileNumber;
	}
	
	/**
	 * Attempt to correct the given number.
	 * @param number
	 * @return
	 */
	public static MobileNumber mobileNumberCorrectionAttempt (String number) {
		
		logger.info("trying to correct number {} ...", number);
		MobileNumber mobileNumber = new MobileNumber();
		mobileNumber.setSourceNumber(number);
		mobileNumber.setAttemptedCorrection(true);
		StringBuilder validTokens = new StringBuilder();
		StringBuilder invalidTokens = new StringBuilder();
		
		// try to separate digits and other characters
		for (int i=0; i<number.length(); i++) {
			var character = number.charAt(i);
			if (Character.isDigit(character)) {
				validTokens.append(character);
			}
			else {
				invalidTokens.append(character);
			}
		}
		String correctionAttempt = validTokens.toString();		
		String dirtyTokens = invalidTokens.toString();
		logger.debug("correctionAttempt: {}", correctionAttempt);
		logger.debug("dirtyTokens: {}", dirtyTokens);
		
		if (MOBILE_NUMBER_FORMAT_REGEX.matcher(correctionAttempt).matches()) {
			mobileNumber.setNumber(correctionAttempt.toString());
			mobileNumber.setStatus(EnumStatusMobileNumber.VALID);
			mobileNumber.setCorrectionDetails("Characters deleted from source number: '" + dirtyTokens + "'");
		}
		
		else {
			mobileNumber.setStatus(EnumStatusMobileNumber.NOT_VALID);
			StringBuilder correctionDetails = new StringBuilder();
			// source number cannot be corrected
			if (correctionAttempt.isEmpty()) {
				correctionDetails.append("Source number cannot be corrected.");
			}			
			// too many digits
			else if (correctionAttempt.length() > DEFAULT_NUMBER_LENGTH) {				
				correctionDetails.append("Too many digits.");
			}
			// too few digits
			else {				
				correctionDetails.append("Too few digits.");
			}
			
			if (!dirtyTokens.isBlank()) {
				correctionDetails.append(" Invalid characters found: '");
				correctionDetails.append(dirtyTokens.toString());
				correctionDetails.append("'.");
			}
			
			mobileNumber.setCorrectionDetails(correctionDetails.toString());
		}
		
		return mobileNumber;
	}
	
}
