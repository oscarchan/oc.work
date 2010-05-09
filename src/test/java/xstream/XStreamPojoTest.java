package xstream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import common.SamplePojo;

public class XStreamPojoTest
{
	private static Log LOG = LogFactory.getLog(XStreamPojoTest.class);
	
	private XStream mXStream;
	
	@Before
	public void init()
	{
//		mXStream = new XStream(newReadWriteJsonDriver());

		mXStream = new XStream(new JettisonMappedXmlDriver());
        mXStream.alias(SamplePojo.class.getSimpleName(), SamplePojo.class);
//        mXStream.alias("list", List.class);
	}
	
	@Test
	public void testObjectSerializing()
	{
        XStream x = mXStream;
        String s = x.toXML(new Object());
    
        LOG.info("object=" + s);
	}
	
	@Test
	public void testSimplePojoSerializing()
	{
        XStream x = mXStream;
		SamplePojo sample = new SamplePojo("sample-id", "sample-name");
		
        String s = x.toXML(sample);
    
        LOG.info("sample json=" + s);
	}

	@Test
	public void testSimplePojoCollectionSerializing()
	{
		List<SamplePojo> samples = Arrays.asList(new SamplePojo("id-1", "name-1"), new SamplePojo("id-2", "name-2")); // , new SamplePojo("id-3", "name-3"), new SamplePojo("id-4", "name-4"));
		
		String s = mXStream.toXML(samples);
		
		LOG.info("sample list: json: " + s);
		
		mXStream.fromXML(s);
	}
		
	@Test
	public void testSimplePojoDeserializing()
	{
		XStream x = mXStream;
		SamplePojo sample = (SamplePojo) x.fromXML("{\"SamplePojo\":{\"id\":\"sample-id\",\"mName\":\"sample-name\"}}");
		
		LOG.info("sample obj=" + sample);
	}	
}