package common.time;


/**
 * model after org.joda.time.Duration with a few simplification 
 */
public class Duration implements Comparable<Duration>
{
	private long timeMillis;
	
	public int compareTo(Duration o)
    {
		if(o==null)
			throw new IllegalArgumentException("missing duration");
		
		return (int) (this.timeMillis - o.timeMillis);
    }

}
