package ws.rs;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/v1")
public class RestSampleService 
{
	@GET
    @Path("/profile/{profileName}")
    @Produces("text/json")
	public Response getProfile(@PathParam("profileName") String id) throws Exception 
	{
		Profile profile = new Profile("747361", "Bjorn", "this is my profile text", "image", true);
		return new Response(id, profile);
	}
	
	/**
	 * Test variation of same path with a single arg
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/sample")
	@Produces("text/json")
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
	

}
