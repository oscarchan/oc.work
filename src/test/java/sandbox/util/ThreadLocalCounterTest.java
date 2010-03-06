package sandbox.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ThreadLocalCounterTest 
{
	@Test
	public void testSimpleThreadCount()
	{
		ThreadLocalCounter<String> counter = new ThreadLocalCounter<String>();
		
		String key1 = "ABC";
		
		assertEquals("incNGet 1", 1, counter.incrementAndGet(key1));
		assertEquals("inc 1", 1, counter.get(key1));
		assertEquals("non exist", 0, counter.get(key1 + "x"));
	}
}
