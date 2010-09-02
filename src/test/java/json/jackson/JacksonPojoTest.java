package json.jackson;

import java.io.IOException;
import java.util.Iterator;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.junit.Before;
import org.junit.Test;

public class JacksonPojoTest
{
	private static Log log = LogFactory.getLog(JacksonPojoTest.class);
	
	@Before
	public void init()
	{
		
	}
	
	@Test
	public void testSimpleObject() throws JsonProcessingException, IOException 
	{
		String sample = "{\"oscarchan1@gmail.com\":\"40000027652\",\"oscarchan2@gmail.com\":40000027666}";
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		JsonNode tree = objectMapper.readTree(sample);
		
			for(Iterator<String> fieldNameItr = tree.getFieldNames();fieldNameItr.hasNext();) {
				String fieldName = fieldNameItr.next();
				
				JsonNode field = tree.get(fieldName);
				
				String stringValue = field.getValueAsText();
				long longValue = field.getLongValue(); // NOTE: long does not work on  
				
				log.info("field: " + fieldName + "=" +stringValue + "/" + longValue);
            }
			
	}

	public void testSimpleArray() throws JsonProcessingException, IOException 
	{
		String sample = "[{\"oscarchan1@gmail.com\":\"40000027652\"},{\"oscarchan2@gmail.com\":\"40000027666\"}]";
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		JsonNode tree = objectMapper.readTree(sample);
		
		Assert.assertTrue("is array", tree.isArray());

		log.info("parsing tree: " + tree);
		
		for(int i=0;i<tree.size();i++) {
			JsonNode jsonNode = tree.get(i);
			
			log.info("parsing node " + i + ": " + jsonNode);
			for(Iterator<String> fieldNameItr = jsonNode.getFieldNames();fieldNameItr.hasNext();) {
				String fieldName = fieldNameItr.next();
				
				JsonNode field = jsonNode.get(fieldName);
				
				String stringValue = field.getValueAsText();
				long longValue = field.getLongValue(); // NOTE: long does not work on  
				
				log.info("field: " + fieldName + "=" +stringValue + "/" + longValue);
            }
		}
	}
	
	
	

}
