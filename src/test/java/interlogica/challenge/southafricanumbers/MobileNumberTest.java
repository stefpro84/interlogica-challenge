package interlogica.challenge.southafricanumbers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import interlogica.challenge.southafricanumbers.entity.EnumStatusMobileNumber;
import interlogica.challenge.southafricanumbers.entity.MobileNumber;
import interlogica.challenge.southafricanumbers.util.MobileNumberUtil;

public class MobileNumberTest {

	
	@Test
	public void checkNumber1() {
				
		MobileNumber mobileNumber = MobileNumberUtil.mobileNumberCorrectionAttempt("12345678");
		
		assertEquals(mobileNumber.getStatus(), EnumStatusMobileNumber.NOT_VALID);
		
	}
	
	@Test
	public void checkNumber2() {
				
		MobileNumber mobileNumber = MobileNumberUtil.mobileNumberCorrectionAttempt("12345678912");
		
		assertEquals(mobileNumber.getStatus(), EnumStatusMobileNumber.VALID);
		
	}
	
	@Test
	public void checkNumber3() {
				
		MobileNumber mobileNumber = MobileNumberUtil.mobileNumberCorrectionAttempt("12345678912aaaaa");
		
		assertEquals(mobileNumber.getStatus(), EnumStatusMobileNumber.VALID);
		
	}
	
	
	@Test
	public void checkNumber4() {
				
		MobileNumber mobileNumber = MobileNumberUtil.mobileNumberCorrectionAttempt("bbbbbb12345678912");
		
		assertEquals(mobileNumber.getStatus(), EnumStatusMobileNumber.VALID);
		
	}
	
	@Test
	public void checkNumber5() {
				
		MobileNumber mobileNumber = MobileNumberUtil.mobileNumberCorrectionAttempt("bbbbbb123456789");
		
		assertEquals(mobileNumber.getStatus(), EnumStatusMobileNumber.NOT_VALID);
		
	}
	
	
	@Test
	public void checkNumber6() {
				
		MobileNumber mobileNumber = MobileNumberUtil.mobileNumberCorrectionAttempt("bbbbbb123456789aaaaa");
		
		assertEquals(mobileNumber.getStatus(), EnumStatusMobileNumber.NOT_VALID);
		
	}
	
	
	@Test
	public void checkNumber7() {
				
		MobileNumber mobileNumber = MobileNumberUtil.mobileNumberCorrectionAttempt("bbb12345678912aaa");
		
		assertEquals(mobileNumber.getStatus(), EnumStatusMobileNumber.VALID);
		
	}
	
	
	
	
}
