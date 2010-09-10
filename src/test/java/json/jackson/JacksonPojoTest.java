package json.jackson;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.User;
import common.SamplePojo;

public class JacksonPojoTest
{
	private static Log log = LogFactory.getLog(JacksonPojoTest.class);

	@Test
	public void testSamplePojo() throws JsonGenerationException,
	        JsonMappingException, IOException
	{
		testPojo("simple-full", new SamplePojo("id-1", "name-1"));
		testPojo("simple-partial", new SamplePojo("id-2", null));
	}

	@Test
	public void testSimpleArrayPojo() throws JsonGenerationException,
	        JsonMappingException, IOException
	{
		SimpleArrayPojo full = new SimpleArrayPojo().setId("id-1").setName(
		        "name-1").setBody(Arrays.asList("a", "b", "c"));
		testPojo("array-full", full);

		SimpleArrayPojo partial = new SimpleArrayPojo().setId("id-1").setName(
		        null).setBody(Arrays.asList("a", null, "c"));
		testPojo("array-full", partial);
	}

	@Test
	public void testSimpleArray() throws JsonGenerationException,
	        JsonMappingException, IOException
	{
		List<Integer> full = new LinkedList<Integer>(Arrays.asList(4, 3, 1, 5,
		        2));
		testPojo("root-array-full", full);
		
		List<Integer> partial = new LinkedList<Integer>(Arrays.asList(4, 3, null, 5,
		        2));
		testPojo("root-array-full", partial);

	}
	
	@Test
	public void testEnumArray() throws JsonGenerationException, JsonMappingException, IOException 
	{
		testPojo("root-enum-array", Arrays.asList((TimeUnit.values())));
	}
	
	@Test
	public void testPojoArray() throws JsonGenerationException, JsonMappingException, IOException 
	{
		List<SamplePojo> empty = new LinkedList<SamplePojo>(Arrays.asList(
		        new SamplePojo(), new SamplePojo(), new SamplePojo(),
		        new SamplePojo(), new SamplePojo()));
		
		// NOTE:  List<Map> is returned instead of List<SamplePojo>
		testPojo("root-pojo-array-empty", empty);

		List<SamplePojo> full = new LinkedList<SamplePojo>(Arrays.asList(
		        new SamplePojo("1", "2"), new SamplePojo("3", "4"), new SamplePojo("5", "6"),
		        new SamplePojo("7", "8")));
		
		testPojo("root-pojo-array-full", full);

		List<SamplePojo> partial = new LinkedList<SamplePojo>(Arrays.asList(
		        new SamplePojo("1", "2"), null, new SamplePojo("5", null),
		        new SamplePojo("7", "8")));
		
		testPojo("root-pojo-array-partial", partial);

	}

	public <T> T testPojo(String prefix, T pojo)
	        throws JsonGenerationException, JsonMappingException, IOException
    {
		ObjectMapper mapper = new ObjectMapper();

		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, pojo);

		log.info(prefix + ": output=" + writer);


		Object parsed = mapper.readValue(writer.toString(), pojo.getClass());

		Assert.assertEquals("pojo", pojo, parsed);

		return (T) parsed;
	}
	
	public <T> T testPojoCollection(String prefix, Collection<T> pojos, Class<T> pojoClass)
	{
		pojoClass =  new TypeReference<Map<String,User>>();
		
		ObjectMapper mapper = new ObjectMapper();

		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, pojos);

		log.info(prefix + ": output=" + writer);


		Object parsed = mapper.readValue(writer.toString(), pojos.getClass());

		Assert.assertEquals("pojo", pojo, parsed);

		return (T) parsed;		
	}

	public void testSimpleObject2() throws JsonProcessingException, IOException
	{
		String sample = "{\"oscarchan1@gmail.com\":\"40000027652\",\"oscarchan2@gmail.com\":40000027666}";

		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode tree = objectMapper.readTree(sample);

		for (Iterator<String> fieldNameItr = tree.getFieldNames(); fieldNameItr
		        .hasNext();) {
			String fieldName = fieldNameItr.next();

			JsonNode field = tree.get(fieldName);

			String stringValue = field.getValueAsText();
			long longValue = field.getLongValue(); // NOTE: long does not work
												   // on

			log.info("field: " + fieldName + "=" + stringValue + "/"
			        + longValue);
		}

	}

	public void testSimpleArray2() throws JsonProcessingException, IOException
	{
		String sample = "[{\"oscarchan1@gmail.com\":\"40000027652\"},{\"oscarchan2@gmail.com\":\"40000027666\"}]";

		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode tree = objectMapper.readTree(sample);

		Assert.assertTrue("is array", tree.isArray());

		log.info("parsing tree: " + tree);

		for (int i = 0; i < tree.size(); i++) {
			JsonNode jsonNode = tree.get(i);

			log.info("parsing node " + i + ": " + jsonNode);
			for (Iterator<String> fieldNameItr = jsonNode.getFieldNames(); fieldNameItr
			        .hasNext();) {
				String fieldName = fieldNameItr.next();

				JsonNode field = jsonNode.get(fieldName);

				String stringValue = field.getValueAsText();
				long longValue = field.getLongValue(); // NOTE: long does not
													   // work on

				log.info("field: " + fieldName + "=" + stringValue + "/"
				        + longValue);
			}
		}
	}
	

	static class SimpleArrayPojo
	{
		private String id;
		private String name;
		private List<String> body;

		public String getId()
		{
			return id;
		}

		public String getName()
		{
			return name;
		}

		public List<String> getBody()
		{
			return body;
		}

		public SimpleArrayPojo setId(String id)
		{
			this.id = id;

			return this;
		}

		public SimpleArrayPojo setName(String name)
		{
			this.name = name;

			return this;
		}

		public SimpleArrayPojo setBody(List<String> body)
		{
			this.body = body;
			return this;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((body == null) ? 0 : body.hashCode());
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SimpleArrayPojo other = (SimpleArrayPojo) obj;
			if (body == null) {
				if (other.body != null)
					return false;
			} else if (!body.equals(other.body))
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

	}

}
