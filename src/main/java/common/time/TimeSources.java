package common.time;

import java.util.Date;

/**
 * provide TimeSource
 */
public class TimeSources
{
	public TimeSource SYSTEM_TIME_SOURCE = new SystemTimeSource();

	
	public static class SystemTimeSource implements TimeSource
	{
		private SystemTimeSource()
		{
			// nothing
		}
		
        public long currentTimeMillis()
        {
	        return System.currentTimeMillis();
        }

        public Date now()
        {
	        return new Date(currentTimeMillis());
        }
		
	}
	
	/**
	 * provider 
	 */
	public static class AdjustableTimeSource implements TimeSource
	{
		private long currentTime;
		
		public long currentTimeMillis()
        {
	        return currentTime;
        }

		public Date now()
        {
	        return new Date(currentTime);
        }
		
		public void setCurrentTime(long currentTime)
        {
	        this.currentTime = currentTime;
        }
		
	}

}
