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
	public void testSplit_multiChars()
	{
		String sample = "abc, xyz, hef";
		
		String[] result1 = sample.split(",");
		String[] result2 = sample.split(", ");
		
		Assert.assertEquals("result 1 size", 3, result1.length);
		Assert.assertEquals("result 1 #1", "abc", result1[0]);
		Assert.assertEquals("result 1 #2", " xyz", result1[1]);
		Assert.assertEquals("result 1 #3", " hef", result1[2]);

		Assert.assertEquals("result 2 size", 3, result2.length);
		Assert.assertEquals("result 2 #1", "abc", result2[0]);
		Assert.assertEquals("result 2 #2", "xyz", result2[1]);
		Assert.assertEquals("result 2 #3", "hef", result2[2]);

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
	
	@Test
	public void testBuilder_vs_Buffer()
	{
	  int numItr = 1000;
	  int sampleSize = 1000;
	  testBuilderPerformance(numItr, sampleSize);
	  testBufferPerformance(numItr, sampleSize);
	}

  private void testBufferPerformance(int numItr, int sampleSize)
  {
    long start = System.currentTimeMillis();
    for (int i = 0; i < numItr; i++) {
      StringBuffer sb = new StringBuffer();
      for (int k = 0; k < sampleSize; k++) {
        sb.append(k);
      }
      
      sb.toString();
    }
    
    long total = System.currentTimeMillis() - start;
    
    mLog.info("buffer : " + numItr + ": sample=" + sampleSize + "total msec=" + total);
  }

  private void testBuilderPerformance(int numItr, int sampleSize)
  {
    long start = System.currentTimeMillis();
    for (int i = 0; i < numItr; i++) {
      StringBuilder sb = new StringBuilder();
      for (int k = 0; k < sampleSize; k++) {
        sb.append(k);
      }
      
      sb.toString();
    }
    
    long total = System.currentTimeMillis() - start;
    
    mLog.info("builder: " + numItr + ": sample=" + sampleSize + "total msec=" + total);
  }
}
