package ws.rs;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.annotations.ClientResponseType;

import ws.rs.ServiceException.ServiceExceptionType;

import common.SamplePojo;

/**
 * PATH example: http://localhost:8000/oc.work/cxf/v1/samples
 */
@Path("/v1")
@Consumes("application/json")
@Produces("application/json")
public class RestSampleService 
{
	@GET
    @Path("/profile/{profileName}")
	public Response getProfile(@PathParam("profileName") String id) throws Exception 
	{
		Profile profile = new Profile("747361", "Bjorn", "this is my profile text", "image", true);
		return new Response(id, profile);
	}
	
	/**
	 * Test batch idempotent insert/update
	 */
	@PUT
	@Path("/samples")
	public Collection<SamplePojo> setSamples(Collection<SamplePojo> samples)
	{
		return samples;
	}
	
	@GET
	@Path("/exceptions/{errorCode}")
	public Collection<SamplePojo> throwException(@PathParam("errorCode") int errorCode)
	{
		ServiceExceptionType type = ServiceExceptionType.getByErrorCode(errorCode);
		
		if(type==null)
			type = ServiceExceptionType.INTERNAL_SERVER_ERROR;
		
		throw new ServiceException(type);
	}
	
	@POST
	
	
	@GET @Path("/samples") 
	public Collection<SamplePojo> getSamples()
	{
		return Arrays.asList(new SamplePojo(), new SamplePojo("id1", "name2"), new SamplePojo("id2", "name2"));
	}
	
	
	
	/**
	 * Test variation of same path with a single arg
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/sample")
	public String getSample(@QueryParam("id") String id) throws Exception
	{
		return "id:" + id;
	}

	@PUT
	@Path("/sample")
	@Produces("text/json") // override the default produces
	public String updateSample(
			@FormParam("id") String id,
			@FormParam("name") String name
			) throws Exception	
	{
		return "update-" + id + "-" + name;
	}
	
	
	@ClientResponseType
	public String testResteasy(Date date)
	{
		return null;
	}
	
	

}
