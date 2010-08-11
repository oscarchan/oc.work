package ws.rs;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.annotations.ClientResponseType;

import common.SamplePojo;


@Path("/v1")
public class RestSampleService 
{
	@GET
    @Path("/profile/{profileName}")
    @Produces("application/json")
	public Response getProfile(@PathParam("profileName") String id) throws Exception 
	{
		Profile profile = new Profile("747361", "Bjorn", "this is my profile text", "image", true);
		return new Response(id, profile);
	}
	
	/**
	 * Test batch idempotent insert/update
	 */
	@PUT @Path("/samples") @Consumes("application/json") @Produces("application/json")
	public Collection<SamplePojo> setSamples(Collection<SamplePojo> samples)
	{
		return samples;
	}
	
	@GET @Path("/samples") @Produces("application/json")
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
	@Produces("application/json")
	public String getSample(@QueryParam("id") String id) throws Exception
	{
		return "id:" + id;
	}

	@PUT
	@Path("/sample")
	@Produces("text/json")
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
