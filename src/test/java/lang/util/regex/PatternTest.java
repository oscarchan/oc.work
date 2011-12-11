package lang.util.regex;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class PatternTest
{
	private static Log log = LogFactory.getLog(PatternTest.class);

	private static final String MINUTE_REGEX = "\\*(/[0-5]?[0-9])?|([0-5]?[0-9])";
    private static final String HOUR_REGEX = "\\*(/[0-2]?[0-9])?|([0-2]?[0-9])";

    public void test6()
    {
		String s = "This is a capturing group: (captured). "
				+ "This is a non-capturing group: (?:not captured)";

		Pattern p = Pattern.compile("(\\w+) (captur(?:ing|ed))");
		Matcher m = p.matcher(s);

		while (m.find()) {
			String matched = m.group();
			System.out.println("Matched: " + matched);
			int n = m.groupCount();
			for (int i = 1; i <= n; i++) {
				String matchgroup = m.group(i);
				System.out.println("Subgroup " + i + ": " + matchgroup);
			}
			System.out.println();
		}    	
    }

	public void test5()
	{
		Pattern pattern = Pattern.compile("a((b)c)");
		
		String raw = "aabc";
		
		Matcher matcher = pattern.matcher(raw);
		
    	while(matcher.find()) { 
    		String group = matcher.group();
    		
    		log.info("group: " + group);
    		log.info("group count: " + matcher.groupCount());
    		
    		for(int i=0;i<matcher.groupCount();i++) { 
    			log.info("group#: " + i + ": " + matcher.group(i));
    		}
    	}
	}
	
	private static Pattern versionCodeRegex = Pattern.compile("([A-Za-z]+)=\"([^\"]+)\"");
	private static Pattern versionNameRegex = Pattern.compile("versionName=\"([0-9]+)");
	private static Pattern packageNameRegex = Pattern.compile("package=\"([^\"]+)");	
	
    @Test
    public void test4()
    {
    	String raw = "<manifest versionCode=\"0x1\" versionName=\"1.0\" package=\"oc.playback\">";
    	
    	Matcher matcher = versionCodeRegex.matcher(raw);
    	
    	while(matcher.find()) { 
    		String group = matcher.group();
    		
    		log.info("group: " + group);
    		log.info("group count: " + matcher.groupCount());
    		
    		for(int i=1;i<=matcher.groupCount();i++) { 
    			log.info("group#: " + i + ": " + matcher.group(i));
    		}
    	}
    	
    }
	public static void test3()
	{
		Pattern pattern = Pattern.compile("([0-9]+)");
		System.out.println("" + pattern);

		Matcher matcher = pattern.matcher("Heartbeat:video:1321607514644");
		// System.out.println("count=" + matcher.groupCount());
		// System.out.println("matches=" + matcher.matches());

		while (matcher.find()) {
			System.out.print("Start index: " + matcher.start());
			System.out.print(" End index: " + matcher.end() + " ");
			System.out.println(matcher.group());
		}
    }
    public static void test2() { 
	Date now = new Date();
	Calendar cal = Calendar.getInstance();
	cal.setTime(now);

	cal.add(Calendar.MINUTE, 10);

	System.out.println("now=" + now);
	System.out.println("cal=" + cal.getTime());


	System.out.println("cal=" + cal.getTime());
	
	System.out.println("after=" + cal.getTime().after(now));
	System.out.println("after=" + cal.getTime().before(now));
    }

    public static void test1() { 
	//	 String expr = "\\*(/[0-5]?[0-9])?|([0-5]?[0-9])";
	String expr = HOUR_REGEX;

	 System.out.println("match=" + "*".matches(expr));
	 System.out.println("match=" + "*/5".matches(expr));
	 System.out.println("match=" + "*/10".matches(expr));
	 System.out.println("match=" + "9".matches(expr));
	 System.out.println("match=" + "45".matches(expr));

	 System.out.println("not match=" + "324".matches(expr));
	 System.out.println("not match=" + "23*".matches(expr));

     }
  

}
