package common;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * pojo for storing primitive, object,  data type
 */
public class BasicDataPojo
{
	private int intValue;
	private long longValue;
	private byte byteValue;
	
	private String StringRef;
	private Integer intRef;
	private Long longRef;
	
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

	public BasicDataPojo getChild()
    {
    	return child;
    }

	public void setIntValue(int intValue)
    {
    	this.intValue = intValue;
    }

	public void setLongValue(long longValue)
    {
    	this.longValue = longValue;
    }

	public void setByteValue(byte byteValue)
    {
    	this.byteValue = byteValue;
    }

	public void setStringRef(String stringRef)
    {
    	StringRef = stringRef;
    }

	public void setIntRef(Integer intRef)
    {
    	this.intRef = intRef;
    }

	public void setLongRef(Long longRef)
    {
    	this.longRef = longRef;
    }

	public void setChild(BasicDataPojo child)
    {
    	this.child = child;
    }
	
	@Override
	public String toString()
	{
	    return ToStringBuilder.reflectionToString(this);
	}
	
}
