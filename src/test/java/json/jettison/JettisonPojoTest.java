package json.jettison;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class JettisonPojoTest
{
	@Test
	public void testSimpleMap() throws JSONException
	{
		String sample = " {\"oscarchan@gmail.com\":\"40000027423\"} ";
	
		JSONObject json = new JSONObject(sample);
		
		Assert.assertTrue("existing key",  json.has("oscarchan@gmail.com"));
		Assert.assertEquals("existing value", "40000027423", json.get("oscarchan@gmail.com"));
		Assert.assertEquals("existing value", 40000027423L, json.getLong(("oscarchan@gmail.com")));
		
		Assert.assertFalse("non-existing key", json.has("sfsfdsf"));
		try {
			Assert.assertNull("non-existing value", json.get("sfsfdsf"));
		} catch (JSONException e) {
			
		}
	}
	
	@Test
	public void testSimpleArray() throws JSONException 
	{
		String sample = "[{\"oscarchan1@gmail.com\":\"40000027652\"},{\"oscarchan2@gmail.com\":\"40000027666\"}]";
		
		JSONObject json = new JSONObject(sample);

	}
}
