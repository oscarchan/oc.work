package common.time;

import java.util.Date;

/**
 * abstract how we obtain the current time, we are not abided by
 * System.currentTimeMillis(). This allows us to fake time increment in unit
 * tests without Thread.sleep(), and also sync up time with another source, such
 * as db
 * 
 * @see TimeSources
 * 
 * @author ochan
 */
public interface TimeSource
{
	// use
	public long currentTimeMillis();

	public Date now();
}

