package spring.beans;

import org.springframework.transaction.annotation.Transactional;

public class ServiceObjects
{
	/**
	 * service with transactional annotation
	 */
	public static class TxMethodService
	{
		@Transactional
		public String getTransactional()
		{
			return "getTransactional";
		}
	
		@Transactional(readOnly = true)
		public String getReadOnly()
		{
			return "getReadOnly";
		}		
	}
	
	/**
	 * service with transaction annotation
	 */
	@Transactional
	public static class TxClassService
	{
		public String getTransactional()
		{
			return "getTransactional";
		}
	}
}
