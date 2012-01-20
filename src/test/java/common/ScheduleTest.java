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
	
	private Schedule testSchedule(String expected, String scheduleExpr, String now) throws ParseException
	{
		Date nowDate = format.parse(now);
		Date expectedDate = format.parse(expected);
		
		Schedule schedule = Schedule.parse(scheduleExpr);
		
		Date scheduledTime = schedule.nextScheduledTime(nowDate);
		
		Assert.assertTrue("future time: now=" + nowDate + ": scheduled=" + scheduledTime, scheduledTime.after(nowDate));
		Assert.assertEquals("next min", expectedDate, scheduledTime);
		
		return schedule;
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
	public void testEveryHourAtZero() throws ParseException 
	{
		String schedule = "0 * * * *";

		String now = "2011-12-16T15:36:27Z";
		String expected = "2011-12-16T16:00:00Z";
		
		testSchedule(expected, schedule, now);
	}

	@Test
	public void testEveryHourAtExactMinute() throws ParseException 
	{
		String schedule = "36 * * * *";

		String now = "2011-12-16T15:36:00Z";
		String expected = "2011-12-16T16:36:00Z";
		
		testSchedule(expected, schedule, now);
	}	
	
	@Test
	public void testEveryHourAtFutureMinute() throws ParseException 
	{
		String schedule = "40 * * * *";

		String now = "2011-12-16T15:36:27Z";
		String expected = "2011-12-16T15:40:00Z";
		
		testSchedule(expected, schedule, now);
	}	

	@Test
	public void testEveryHourAtPasteMinute() throws ParseException 
	{
		String schedule = "30 * * * *";

		String now = "2011-12-16T15:36:27Z";
		String expected = "2011-12-16T16:30:00Z";
		
		testSchedule(expected, schedule, now);
	}
	
	@Test
	public void testEvery2Hour() throws ParseException{
		String schedule = "0 */2 * * *";

		String now = "2011-12-16T15:36:27Z";
		String expected = "2011-12-16T16:00:00Z";
		testSchedule(expected, schedule, now);
		
		schedule = "0 */3 * * *";
		now = "2011-12-16T15:36:27Z";
		expected = "2011-12-16T18:00:00Z";
		testSchedule(expected, schedule, now);
		
	}
	
	@Test
	public void testFixedHourMinute() throws ParseException 
	{
		String schedule = "30 0 * * *";

		String now = "2011-12-16T15:36:27Z";
		String expected = "2011-12-17T00:30:00Z";
		
		testSchedule(expected, schedule, now);
	}		
}
