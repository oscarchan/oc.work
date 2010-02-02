package lang;

import java.util.Date;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class StringTest
{
	private static final Log mLog = LogFactory.getLog(StringTest.class);
	
	@Test
	public void testConstant()
	{
		String s1 = "abc";
		String s2 = "abc";
		
		mLog.info("s1==s2: " + (s1==s2));
	}
	
	@Test
	public void testNumber()
	{
	  compareNumber(1, 2);
	  compareNumber(12, 2);
	}
	
	private void compareNumber(int a, int b)
	{
	  mLog.info(a + ".compareTo(" + b + ")=" + String.valueOf(a).compareTo(String.valueOf(b)));
	}
	
	@Test
	public void testFormat()
	{
//		String template = "%1$ty-%1$tm-%1$td %1$tH:%1$tM (%2f, %3f)";
		String template = "%1$tF %1$tT (%2$f %3$f)";
		
		mLog.info("date=" + String.format(template, new Date(), 3.0, 2.0));
	}
	
	@Test
	public void testSplit_empty()
	{
		String[] result = "".split("/");
		
		Assert.assertNotNull("split result", result);
		for (String token : result) {
			mLog.info("token=" + token);
		}
		
	}
}