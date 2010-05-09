package common;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("SamplePojo")
public class SamplePojo
{
	private static Log LOG = LogFactory.getLog(SamplePojo.class);
	
	private String id;
	private String mName;
	
	public SamplePojo(String id, String name)
	{
		this.id = id;
		mName = name;
	}
	
	public SamplePojo()
	{
		// nothing
	}
	
	public String getId()
    {
    	return id;
    }

	public void setId(String id)
    {
		LOG.debug("setId(" + id + ")");
    	this.id = id;
    }
	public String getName()
    {
    	return mName;
    }

	public void setName(String name)
    {
    	mName = name;
    }

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
}

