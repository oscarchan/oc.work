package json.jackson;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import junit.framework.Assert;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.junit.Test;

import common.RandomPojoTestUtils;
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
	public void testDateArray() throws JsonParseException, JsonMappingException, IOException 
	{
		List<Date> dates = Arrays.asList(new Date(), new Date());
		
		testPojo("data-array", dates);
	}
	/**
	 * test composite
	 */
	@Test
	public void testBasicDataPojo() throws JsonGenerationException, JsonMappingException, IOException 
	{
		testPojo("simple-basic-pojo=full", RandomPojoTestUtils.randomBasicDataPojo(0));
		testPojo("simple-basic-pojo=full", RandomPojoTestUtils.randomBasicDataPojo(1));
		testPojo("simple-basic-pojo=full", RandomPojoTestUtils.randomBasicDataPojo(2));
		testPojo("simple-basic-pojo=full", RandomPojoTestUtils.randomBasicDataPojo(4));
		testPojo("simple-basic-pojo=full", RandomPojoTestUtils.randomBasicDataPojo(16));
		
	}

	@Test
	public void testPolyMorphism() throws JsonGenerationException, JsonMappingException, IOException
	{
		Circle circle= new Circle();
		circle.setRadius(2);

		testPojoSuper("poly-circle", circle, Shape.class);
	}
	
	

	@Test
	public void testSimpleArrayPojo() throws JsonGenerationException,
	        JsonMappingException, IOException
	{
		SimpleArrayPojo full = new SimpleArrayPojo().setId("id-1").setName(
		        "name-1").setBody(Arrays.asList("a", "b", "c")).setLongFieldName("very long name");
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
	
	public <T> T testPojoSuper(String prefix, T pojo, Class<? super T> clazz)
	        throws JsonGenerationException, JsonMappingException, IOException
    {
		ObjectMapper mapper = getObjectMapper();

		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, pojo);

		log.info(prefix + ": output=" + writer);


		Object parsed = mapper.readValue(writer.toString(), clazz);

		Assert.assertEquals("pojo", pojo, parsed);

		return (T) parsed;
	}

	private ObjectMapper getObjectMapper()
    {
	    ObjectMapper mapper = new ObjectMapper();
		
	    AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
	    AnnotationIntrospector secondary = new JaxbAnnotationIntrospector();
	    AnnotationIntrospector introspector = new AnnotationIntrospector.Pair(primary, secondary);

	    mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
	    mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
	    return mapper;
    }
	
	public <T> T testPojo(String prefix, T pojo) throws JsonParseException, JsonMappingException, IOException
	{
		if(pojo==null)
			throw new IllegalArgumentException("missing pojo");
		
//		pojoClass =  new TypeReference<Map<String,User>>();
		
		JavaType type;
		
		if (pojo.getClass().isArray() && Array.getLength(pojo) > 0) {
			type = TypeFactory.arrayType(Array.get(pojo, 0).getClass());
		} else if (pojo instanceof List) {  
			Object next = ((List) pojo).iterator().next();
			
			// all list use ArrayList
			type = TypeFactory.collectionType(ArrayList.class, next.getClass());
		} else if (pojo instanceof Map) { 
			throw new UnsupportedOperationException("Map is not supported: " + pojo);
		} else { 
			type = TypeFactory.type(pojo.getClass());
		}

		
		ObjectMapper mapper = getObjectMapper();

		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, pojo);

		log.info(prefix + ": output=" + writer);


		Object parsed = mapper.readValue(writer.toString(), type);

		Assert.assertEquals("pojo", pojo, parsed);

		return (T) parsed;
	}

	public void testSimpleObject2() throws JsonProcessingException, IOException
	{
		String sample = "{\"oscarchan1@gmail.com\":\"40000027652\",\"oscarchan2@gmail.com\":40000027666}";

		ObjectMapper objectMapper = getObjectMapper();

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

		ObjectMapper objectMapper = getObjectMapper();

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
	
	/**
	 *  polymorphic type handling solution 1: storing the type info as property via annotation
	 *  
	 *  @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
	 *  class Parent {}
	 */

	/**
	 *  polymorphic type handling solution 2: storing the type info as property via mapper property
	 *  
     * mapper.enableDefaultTyping(); // defaults for defaults (see below); include as wrapper-array, non-concrete types
     * mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT); // all non-final types
	 */
	
	/**
	 * polymorphic type handling solution 3:
	 * 
	 *  @JsonTypeInfo in the super class, @JsonSubTypes and @JsonTypeName in the subclasses 
	 */

	@JsonTypeInfo(use=Id.NAME, include=As.PROPERTY, property="type")
	@JsonSubTypes ( { 
		@Type(value = Circle.class, name = "circle"),
		@Type(value = Polygon.class, name = "polygon")	
		}
	)
	static class Shape
	{
		private String name;

		public String getName()
        {
        	return name;
        }

		public void setName(String name)
        {
        	this.name = name;
        }

		@Override
        public int hashCode()
        {
	        final int prime = 31;
	        int result = 1;
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
	        Shape other = (Shape) obj;
	        if (name == null) {
		        if (other.name != null)
			        return false;
	        } else if (!name.equals(other.name))
		        return false;
	        return true;
        }
		
		@Override
		public String toString()
		{
		    return ToStringBuilder.reflectionToString(this);
		}
	}
	
	@JsonTypeName("polygon")
	static class Polygon extends Shape
	{
		
		private int numEdges;
		private int area;

		public Polygon()
        {
			setName("polygon");
        }
		
		public int getNumEdges()
        {
        	return numEdges;
        }
		public int getArea()
        {
        	return area;
        }
		public void setNumEdges(int numEdges)
        {
        	this.numEdges = numEdges;
        }
		public void setArea(int area)
        {
        	this.area = area;
        }
		@Override
        public int hashCode()
        {
	        final int prime = 31;
	        int result = super.hashCode();
	        result = prime * result + area;
	        result = prime * result + numEdges;
	        return result;
        }
		@Override
        public boolean equals(Object obj)
        {
	        if (this == obj)
		        return true;
	        if (!super.equals(obj))
		        return false;
	        if (getClass() != obj.getClass())
		        return false;
	        Polygon other = (Polygon) obj;
	        if (area != other.area)
		        return false;
	        if (numEdges != other.numEdges)
		        return false;
	        return true;
        }
	}
	
	@JsonTypeName("circle")
	static class Circle extends Shape
	{
		private int radius;

		public Circle()
        {
			setName("circle");
        }
		public int getRadius()
        {
        	return radius;
        }

		public void setRadius(int radius)
        {
        	this.radius = radius;
        }

		@Override
        public int hashCode()
        {
	        final int prime = 31;
	        int result = super.hashCode();
	        result = prime * result + radius;
	        return result;
        }

		@Override
        public boolean equals(Object obj)
        {
	        if (this == obj)
		        return true;
	        if (!super.equals(obj))
		        return false;
	        if (getClass() != obj.getClass())
		        return false;
	        Circle other = (Circle) obj;
	        if (radius != other.radius)
		        return false;
	        return true;
        }
		
		
	}
	
	static class Square extends Polygon
	{
		
	}
	
	
	@XmlAccessorType(XmlAccessType.FIELD)
	static class SimpleArrayPojo
	{
		private String id;
		private String name;
		private List<String> body;
		private String long_field_name;

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

		public String getLongFieldName()
        {
	        return long_field_name;
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
		
		public SimpleArrayPojo setLongFieldName(String fieldName)
        {
	        this.long_field_name = fieldName;
	        return this;
        }

		@Override
        public int hashCode()
        {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + ((body == null) ? 0 : body.hashCode());
	        result = prime * result + ((id == null) ? 0 : id.hashCode());
	        result = prime
	                * result
	                + ((long_field_name == null) ? 0 : long_field_name
	                        .hashCode());
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
	        if (long_field_name == null) {
		        if (other.long_field_name != null)
			        return false;
	        } else if (!long_field_name.equals(other.long_field_name))
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
