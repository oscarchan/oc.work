package lang.text;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class MessageFormatTest
{
	private static Log log = LogFactory.getLog(MessageFormatTest.class);
	
	@Test
	public void test1()
	{
		MessageFormat template = new MessageFormat("'legacy_account.'yyyy-MM-dd-HH:mm:ss'.csv'");
		
		String text = template.format(new Object[] { new Date()});
		
		log.info("text=" + text);
	}
	
	@Test
	public void test2()
	{
		DateFormat template = new SimpleDateFormat("'legacy_account.'yyyy-MM-dd-HH:mm:ss'.csv'");
		
		String text = template.format(new Date());
		
		log.info("text=" + text);
	}
}
