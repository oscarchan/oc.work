package validation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


public class ValidProfile
{
	@Min(value=0) 
	private int id;
	
	@NotNull
	private String name;

	public int getId()
    {
    	return id;
    }

	public String getName()
    {
    	return name;
    }

	public void setId(int id)
    {
    	this.id = id;
    }

	public void setName(String name)
    {
    	this.name = name;
    }
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this,
		        ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
