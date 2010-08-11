package spymemcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Future;

import junit.framework.Assert;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.IntegerTranscoder;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

public class MemcachedTest
{
	private static int portNum = 11211;
	private final static IntegerTranscoder TO_INT = new IntegerTranscoder();
	
	@Test
	public void testGet() throws IOException
	{
		MemcachedClient c=new MemcachedClient(new InetSocketAddress("localhost", portNum));

		String key = RandomStringUtils.randomAlphanumeric(10);
		Future<Integer> asyncGet = c.asyncGet(key, TO_INT);
		
		Integer count = c.get(key, TO_INT);
		
		
		Assert.assertEquals("key: " + key, null, count);
		
		long newCount = c.incr(key, 1, 1, 1000);
		
		Assert.assertEquals("key: " + key, 1, newCount);
		
		count =  c.get(key, TO_INT);
		
		Assert.assertEquals("key: " + key, new Integer(1), count);		
	}

}
