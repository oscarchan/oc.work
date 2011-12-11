package common;

/**
 * Essentially cron expression
 */ 
import java.util.Calendar;
import java.util.Date;

import lang.StringTest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Schedule
{
	private static final Log mLog = LogFactory.getLog(StringTest.class);
	
	private static final String TAG = "hb.Schedule";
	private static final String MINUTE_REGEX = "\\*(/[0-5]?[0-9])?|([0-5]?[0-9])";
	private static final String HOUR_REGEX = "\\*(/[0-2]?[0-9])?|([0-2]?[0-9])";
	
	private String hour;
	private String min;
	
	public static Schedule parse(String expression)
	{
		if(expression==null || expression.trim().isEmpty())
			return null;
		
		String[] split = expression.split("\\s+");
		if(split.length < 2) {
			mLog.info("unable to parse cron expr: " + expression);
			return null;
		}
		
		Schedule config = new Schedule();
		
		config.min = split[0];
		config.hour = split[1];
		
		if(config.isValid()==false) {  
			mLog.info("invalid cron expr: " + expression);
			return null;
		}
		
		return config;
	}

	public boolean isScheduledTime(Date time)
	{
		return false;
	}
	public boolean isValid()
	{
		return min.matches(MINUTE_REGEX) && hour.matches(HOUR_REGEX);
	}
	
	public long getRecurringInterval()
	{
		Integer minInterval = getPerInterval(this.min);
		if(minInterval!=null)
			return minInterval * 60 * 1000L;
		else
			return 24 * 60 * 60 * 1000L; // 1 day
	}
	
	public int getPerHourInterval()
	{
		return getPerInterval(hour);
	}
	
	public int getPerMinuteInterval()
	{
		return getPerInterval(min);
	}

	public boolean isFixedHour()
	{
		return hour.matches("[0-9]+");
	}
	
	public boolean isFixedMinute()
	{
		return min.matches("[0-9]+");
	}
	/**
	 * next scheduled time since given time
	 */ 
	public Date nextScheduledTime(Date now)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);

		if(getPerHourInterval()==1 && getPerMinuteInterval()==1) {  // e.g. * * * * *  
			cal.add(Calendar.MINUTE, 1); // move to next minute 
		} else if(getPerHourInterval()==1 && getPerMinuteInterval() > 1) { // every X mins e.g. */5 * * * *
			int minIncrement = getNextMinuteIncrement(cal);
			cal.add(Calendar.MINUTE, minIncrement);
		} else if(getPerHourInterval() >= 1 && getPerMinuteInterval() >= 1) { // every X hours at every Y minutes. e.g.  */5 */2 * * *
			if(matchOnlyHour(cal)) {  //if hour matches
				cal.add(Calendar.MINUTE, getNextMinuteIncrement(cal));
				
				if(matchOnlyHour(cal)==false) { // if the increment hits the next hour and it 
					cal.set(Calendar.MINUTE, 0);
					cal.add(Calendar.HOUR_OF_DAY, getNextHourIncrement(cal));
				} 
			} else { // if hour does not match
				cal.set(Calendar.MINUTE, 0);
				cal.add(Calendar.HOUR_OF_DAY, getNextHourIncrement(cal));
			}
		} else if(getPerHourInterval() >= 1 && getPerMinuteInterval() == 0) {  // every X hours at Y minute. e.g. 30 */2 * * *
			cal.set(Calendar.MINUTE, getFixedMinute());
				
			if(matchOnlyHour(cal)==false // if 1) hour is not right,
				|| cal.getTime().after(now)==false) {  // or 2) hour is right but it is in the past  
					cal.add(Calendar.HOUR_OF_DAY, getNextHourIncrement(cal));
			}
		} else if (getPerHourInterval()==0 && getPerMinuteInterval()==0) {  // e.g. 22 0 * *  *
			cal.set(Calendar.HOUR_OF_DAY, getFixedHour());
			cal.set(Calendar.MINUTE, getFixedMinute());
			
			if(cal.getTime().after(now)==false)  
				cal.add(Calendar.DAY_OF_YEAR, 1);
		} else {
			// not knowing what to do
			mLog.info("unable to calculate next schedule time: " + this + ": time=" + now);
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		// clean up the sub minute
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
	}
	
	/**
	 * @param expr
	 * @return  
	 *    1 if expr == *
	 *    X if expr == * / X
	 *    0 if expr ~=  [0-9]+  
	 */
	private static int getPerInterval(String expr)
	{
		if(expr.equals("*"))
			return 1;
			
		String[] split = expr.split("[ ]*/[ ]*");
		
		if(split.length==2)
			return Integer.parseInt(split[1]);
		
		return 0;
	}
	
	private int getFixedHour()
	{
		return Integer.parseInt(hour);
	}
	
	private int getFixedMinute()
	{
		return Integer.parseInt(min);
	}
	
	private boolean matchOnlyHour(Calendar time)
	{
		int perHour = getPerHourInterval();
		int hour = time.get(Calendar.HOUR_OF_DAY);
		
		if(perHour==0 && hour==getFixedHour())
			return true;
		else if (perHour==1)
			return true;
		else if (hour % perHour==0)
			return true;
		
		return false;
	}

	private boolean matchOnlyMinute(Calendar time)
	{
		int perMinute = getPerMinuteInterval();
		int minute = time.get(Calendar.MINUTE);
		
		if(perMinute==0 && minute==getFixedMinute())
			return true;
		else if (perMinute==1)
			return true;
		else if (minute % perMinute==0)
			return true;
		
		return false;
	}
	
	public int getNextMinuteIncrement(Calendar time)
	{
		int currMin = time.get(Calendar.MINUTE);
		
		int nextMin = (currMin / getPerMinuteInterval() + 1) * getPerMinuteInterval();
		
		return nextMin - currMin;
	}

	public int getNextHourIncrement(Calendar time)
	{
		int currHour = time.get(Calendar.HOUR_OF_DAY);
		
		int nextHour = (currHour/ getPerHourInterval() + 1) * getPerHourInterval();
		
		return nextHour - currHour;
	}

	@Override
	public String toString() {
		return "schd[" + min + " " + hour + "]";
	}
	
}
