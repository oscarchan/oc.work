package spring.beans;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

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
		public String getMemberFriend(@PathParam("id") long id, @QueryParam("fid") long fid)
		{
			return String.valueOf(fid);
		}
		
		@GET
		@Path("/members/{id}/messages/{mid}")
		public String getMemberMessage(@PathParam("id") long id, @PathParam("fid") long mid)
		{
			return String.valueOf(mid);
		}
		
		public String throwException(WebApplicationException e) throws WebApplicationException
		{
			throw e;
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
