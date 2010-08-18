package spymemcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import junit.framework.Assert;
import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.IntegerTranscoder;
import net.spy.memcached.transcoders.LongTranscoder;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.mchange.v2.log.MLog;


public class MemcachedTest
{
	private static int portNum = 11211;
	private final static IntegerTranscoder TO_INT = new IntegerTranscoder();
	private final static LongTranscoder TO_LONG = new LongTranscoder();

	private MemcachedClient cache;
	
	@Before
	public void init() throws IOException 
	{
		ConnectionFactory connectionFactory 
			= new ConnectionFactoryBuilder()
			.setOpTimeout(100L)  // 100 ms for timeout test case
			.setDaemon(true) 
			.setProtocol(Protocol.BINARY)
			.build();
		
		cache =new MemcachedClient(connectionFactory, Arrays.asList(new InetSocketAddress("localhost", portNum)));
		
	}

	public void testGet() throws IOException
	{
		MemcachedClient c=cache;

		String key = RandomStringUtils.randomAlphanumeric(10);
		
		Integer count = c.get(key, TO_INT);
		
		
		Assert.assertNull("key: " + key, count);
		
		long newCount = c.incr(key, 1, 1, 1000);
		
		Assert.assertEquals("key: " + key, 1, newCount);
		
		count =  c.get(key, TO_INT);
		
		Assert.assertEquals("key: " + key, new Integer(1), count);		
	}
	
	@Test
	public void testGets() throws IOException
	{
		MemcachedClient c=cache;

		String keyInt = RandomStringUtils.randomAlphanumeric(10);
		String keyLong = RandomStringUtils.randomAlphanumeric(10);

		Map<String, Long> values = c.getBulk(TO_LONG, keyInt, keyLong);
		
		Long valueInt = values.get(keyInt);
		Long  valueLong = values.get(keyLong);
		
		Assert.assertNull("keyInt: " + keyInt, valueInt);
		Assert.assertNull("keyLong: " + keyLong, valueLong);
		
		
	}
	
	@Test
	public void testAsyncGet() throws IOException, InterruptedException, ExecutionException, TimeoutException
	{
		MemcachedClient c=cache;	
		
		String key = RandomStringUtils.randomAlphanumeric(10);
		Future<Integer> asyncGet = c.asyncGet(key, TO_INT);
		
		Integer count = asyncGet.get(1, TimeUnit.SECONDS);
		
		Assert.assertNull("key: " + key, count);

		Future<Long> asyncIncr = c.asyncIncr(key, 1);
		
		Assert.assertNull("key: " + key, asyncIncr.get(1, TimeUnit.SECONDS));
		
		c.set(key, 1000, 1, TO_INT);
		
		count =  c.get(key, TO_INT);
		
		Assert.assertEquals("key: " + key, new Integer(1), count);				
	}

	@Test
	public void testAppend() throws InterruptedException, ExecutionException 
	{
		MemcachedClient c=cache;
		
		String key = RandomStringUtils.randomAlphanumeric(10);

		Future<Boolean> set = c.set(key, 60, "abc");
		
		Assert.assertEquals("set return", Boolean.TRUE, set.get());
		
		c.append(0, key, "123");
		
		MLog.info("final value: " + c.get(key));
	}

}
