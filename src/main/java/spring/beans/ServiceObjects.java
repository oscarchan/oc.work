package spring.beans;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.springframework.transaction.annotation.Transactional;

public class ServiceObjects
{
	
	public static class RestMemberService
	{
		@GET
		@Path("/members/{id}")
		public String getMember(@PathParam("id") long id)
		{
			return String.valueOf(id);
		}
		
		@GET
		@Path("/members/{id}/friends/{fid}")
		public String getMemberFriend(@PathParam("id") long id, @PathParam("id") long fid)
		{
			return String.valueOf(fid);
		}
		
		
	}
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
