package common;


import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;

import org.apache.commons.lang.math.RandomUtils;

public class RandomTestUtils
{
	public static <T extends Enum<T>> T nextEnum(Class<T> enumClass)
	{
		ArrayList<T> list = new ArrayList<T>(EnumSet.allOf(enumClass));

		if (list.isEmpty())
			throw new IllegalArgumentException("missing enum values: "
			        + enumClass);

		T entry = list.get(RandomUtils.nextInt(list.size()));

		return entry;
	}
	
	public static int nextInt(int max)
	{
		return nextInt(0, max);
	}
	
	public static int nextInt(int start, int end)
	{
		int random = RandomUtils.nextInt(end - start);
		
		return random + start;
	}
	
	public static long nextLong(long start, long end)
	{
		long random = (long) Math.abs(RandomUtils.nextFloat() * (end - start + 1));
		
		random += end;
		
		return random;
	}
	
	public static Date nextDate()
	{
		return new Date(System.currentTimeMillis() + RandomUtils.nextInt(10000));
	}
}

