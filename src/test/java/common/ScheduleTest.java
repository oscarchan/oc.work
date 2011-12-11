package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class ScheduleTest
{
	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); 

	@Test
	public void testEveryMin() throws ParseException
	{
		String schedule = "* * * * *";

		String now = "2011-12-16T15:36:27Z";
		String expected = "2011-12-16T15:37:00Z";
		
		testSchedule(expected, schedule, now);
	}
	
	private void testSchedule(String expected, String scheduleExpr, String now) throws ParseException
	{
		Date nowDate = format.parse(now);
		Date expectedDate = format.parse(expected);
		
		Schedule schedule = Schedule.parse(scheduleExpr);
		
		Date scheduledTime = schedule.nextScheduledTime(nowDate);
		
		Assert.assertTrue("future time: now=" + nowDate + ": scheduled=" + scheduledTime, scheduledTime.after(nowDate));
		Assert.assertEquals("next min", expectedDate, scheduledTime);		
	}
	@Test
	public void testEvery5Min() throws ParseException
	{
		String schedule = "*/5 * * * *";

		String now = "2011-12-16T15:36:27Z";
		String expected = "2011-12-16T15:40:00Z";
		
		testSchedule(expected, schedule, now);
	}
	
	@Test
	public void testEveryHour() throws ParseException 
	{
		String schedule = "0 * * * *";

		String now = "2011-12-16T15:36:27Z";
		String expected = "2011-12-16T16:00:00Z";
		
		testSchedule(expected, schedule, now);
	}

}
