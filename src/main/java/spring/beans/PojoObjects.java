package spring.beans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PojoObjects
{
	/** bean with a simple name property */
	public static class SamplePojo
	{
		private static final Log mLog = LogFactory.getLog(SamplePojo.class);
		
		private String name;
		
		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}
	}
}
