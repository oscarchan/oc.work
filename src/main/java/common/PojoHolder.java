package common;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Test object for generic 
 * @param <T>
 */
public class PojoHolder<T>
{
	private T pojo;
	
	public static <T> PojoHolder<T> wrap(T pojo)
	{
		PojoHolder<T> holder = new PojoHolder<T>();
		
		holder.pojo = pojo;
		
		return holder;
	}
	
	@Override
	public String toString()
	{
	    return ToStringBuilder.reflectionToString(this);
	}
}
