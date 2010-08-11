package json.xstream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

import common.PojoHolder;
import common.SamplePojo;

public class XStreamPojoTest
{
	private static Log log = LogFactory.getLog(XStreamPojoTest.class);
	
	@Rule public TestName testName = new TestName();
	private XStream mXStream;
	
	@Before
	public void init()
	{
//		mXStream = new XStream(newReadWriteJsonDriver());
//		mXStream = new XStream(new JettisonMappedXmlDriver());
		mXStream = new XStream(new JsonHierarchicalStreamDriver());
		
        mXStream.alias(SamplePojo.class.getSimpleName(), SamplePojo.class);
//        mXStream.alias("list", List.class);
	}
	
	@Test
	public void testObject()
	{
        XStream x = mXStream;
        String s = x.toXML(new Object());
    
        log.info("object=" + s);
        
		SamplePojo sample = (SamplePojo) x.fromXML(s);
		
		log.info("sample obj=" + sample);
	}
	
	@Test
	public void testSimplePojo()
	{
        XStream x = mXStream;
		SamplePojo sample = new SamplePojo("sample-id", "sample-name");
		
        String s = x.toXML(sample);
    
        log.info("sample json=" + s);
	}

	@Test
	public void testSimplePojoList()
	{
		List<SamplePojo> samples = Arrays.asList(new SamplePojo("id-1", "name-1"), new SamplePojo("id-2", "name-2")); // , new SamplePojo("id-3", "name-3"), new SamplePojo("id-4", "name-4"));

		List<SamplePojo> list = new ArrayList<SamplePojo>(samples);

		String s = mXStream.toXML(list);
		
		log.info("sample list: json: " + s);
		
		mXStream.fromXML(s);
	}
	
	@Test
	public void testSimplePojoEmbeddedList()
	{
		List<SamplePojo> samples = Arrays.asList(new SamplePojo("id-1", "name-1"), new SamplePojo("id-2", "name-2")); // , new SamplePojo("id-3", "name-3"), new SamplePojo("id-4", "name-4"));
		List<SamplePojo> list = new ArrayList<SamplePojo>(samples);
		
		PojoHolder<List<SamplePojo>> holder = PojoHolder.wrap(list);
		
		// mXStream.addImplicitCollection(PojoHolder.class, "pojo"); // does not work
		mXStream.omitField(PojoHolder.class, "pojo");
		
		String s = mXStream.toXML(holder);
		
		log.info("testSimplePojoEmbeddedList: sample list: json: " + s);
	}
		
		
}