package validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ValidationTest
{
	private static Log logger = LogFactory.getLog(ValidationTest.class);
	
	private Validator validator;
	
	@Before
	public void setUp()
	{
//		Validation.byDefaultProvider()
//			.providerResolver(new Os())
//			.configure();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		
		validator = factory.getValidator();
		
	}
	@Test
	public void testSuccess()
	{
		ValidProfile profile = new ValidProfile();
		profile.setId(123);
		profile.setName("abc");
		
		
		Set<ConstraintViolation<ValidProfile>> violation = validator.validate(profile);

		Assert.assertEquals("violations: " + violation, 0, violation.size());
		
	}
	
	@Test
	public void testFailure()
	{
		ValidProfile profile = new ValidProfile();
		
		Set<ConstraintViolation<ValidProfile>> violation = validator.validate(profile);

		logger.info("violations: " + violation);
	}

}
