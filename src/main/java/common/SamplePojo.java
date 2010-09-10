package common;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
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
    public int hashCode()
    {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((id == null) ? 0 : id.hashCode());
	    result = prime * result + ((mName == null) ? 0 : mName.hashCode());
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
	    SamplePojo other = (SamplePojo) obj;
	    if (id == null) {
		    if (other.id != null)
			    return false;
	    } else if (!id.equals(other.id))
		    return false;
	    if (mName == null) {
		    if (other.mName != null)
			    return false;
	    } else if (!mName.equals(other.mName))
		    return false;
	    return true;
    }

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
}

