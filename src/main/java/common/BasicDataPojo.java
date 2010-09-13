package common;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * pojo for storing primitive, object,  data type
 */
public class BasicDataPojo
{
	private int intValue;
	private long longValue;
	private byte byteValue;
	
	private Integer intRef;
	private Long longRef;
	
	private String StringRef;
	private TimeUnit enumRef;
	
	// recursive pojo
	private BasicDataPojo child;

	public int getIntValue()
    {
    	return intValue;
    }

	public long getLongValue()
    {
    	return longValue;
    }

	public byte getByteValue()
    {
    	return byteValue;
    }

	public String getStringRef()
    {
    	return StringRef;
    }

	public Integer getIntRef()
    {
    	return intRef;
    }

	public Long getLongRef()
    {
    	return longRef;
    }

	public TimeUnit getEnumRef()
    {
	    return enumRef;
    }
	
	public BasicDataPojo getChild()
    {
    	return child;
    }

	public BasicDataPojo setIntValue(int intValue)
    {
    	this.intValue = intValue;
    	
    	return this;
    }

	public BasicDataPojo setLongValue(long longValue)
    {
    	this.longValue = longValue;
    	return this;
    }

	public BasicDataPojo setByteValue(byte byteValue)
    {
    	this.byteValue = byteValue;
    	return this;
    }

	public BasicDataPojo setStringRef(String stringRef)
    {
    	StringRef = stringRef;
    	return this;
    }

	public BasicDataPojo setIntRef(Integer intRef)
    {
    	this.intRef = intRef;
    	return this;
    }

	public BasicDataPojo setLongRef(Long longRef)
    {
    	this.longRef = longRef;
    	return this;
    }

	public BasicDataPojo setEnumRef(TimeUnit enumRef)
    {
	    this.enumRef = enumRef;
	    return this;
    }
	
	public BasicDataPojo setChild(BasicDataPojo child)
    {
    	this.child = child;
    	return this;
    }
	
	@Override
	public String toString()
	{
	    return ToStringBuilder.reflectionToString(this);
	}

	@Override
    public int hashCode()
    {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result
	            + ((StringRef == null) ? 0 : StringRef.hashCode());
	    result = prime * result + byteValue;
	    result = prime * result + ((child == null) ? 0 : child.hashCode());
	    result = prime * result + ((enumRef == null) ? 0 : enumRef.hashCode());
	    result = prime * result + ((intRef == null) ? 0 : intRef.hashCode());
	    result = prime * result + intValue;
	    result = prime * result + ((longRef == null) ? 0 : longRef.hashCode());
	    result = prime * result + (int) (longValue ^ (longValue >>> 32));
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
	    BasicDataPojo other = (BasicDataPojo) obj;
	    if (StringRef == null) {
		    if (other.StringRef != null)
			    return false;
	    } else if (!StringRef.equals(other.StringRef))
		    return false;
	    if (byteValue != other.byteValue)
		    return false;
	    if (child == null) {
		    if (other.child != null)
			    return false;
	    } else if (!child.equals(other.child))
		    return false;
	    if (enumRef != other.enumRef)
		    return false;
	    if (intRef == null) {
		    if (other.intRef != null)
			    return false;
	    } else if (!intRef.equals(other.intRef))
		    return false;
	    if (intValue != other.intValue)
		    return false;
	    if (longRef == null) {
		    if (other.longRef != null)
			    return false;
	    } else if (!longRef.equals(other.longRef))
		    return false;
	    if (longValue != other.longValue)
		    return false;
	    return true;
    }
	
	
}
