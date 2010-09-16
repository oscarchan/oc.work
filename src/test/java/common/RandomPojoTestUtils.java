package common;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;

import common.BasicDataPojo;


public class RandomPojoTestUtils
{
	
	public static BasicDataPojo randomBasicDataPojo(int childDepth)
	{
		if(childDepth<0)
			return null;
		
		int intValue = RandomUtils.nextInt();
		long longValue = RandomUtils.nextLong();
		BasicDataPojo pojo = new BasicDataPojo()
			.setIntValue(intValue)
			.setLongValue(longValue)
			.setByteValue((byte) RandomUtils.nextInt())
			.setIntRef(RandomUtils.nextBoolean()?intValue:null)
			.setLongRef(RandomUtils.nextBoolean()?longValue:null)
			.setStringRef(RandomStringUtils.randomAlphanumeric(5))
			.setEnumRef(RandomTestUtils.nextEnum(TimeUnit.class))
			.setChild(randomBasicDataPojo(childDepth - 1))
			;
		
		return pojo;
	}
}
