package sandbox.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * counter to keep track of usage separately with each threads
 * @author ochan
 *
 */
public class ThreadLocalCounter<T> 
{
	private ThreadLocal<ConcurrentHashMap<T, AtomicInteger>> mCounts = new ThreadLocal<ConcurrentHashMap<T, AtomicInteger>>()
	{
		@Override
		protected ConcurrentHashMap<T, AtomicInteger> initialValue() 
		{
			return new ConcurrentHashMap<T, AtomicInteger>();
		}		
	};
	
	public int incrementAndGet(T key)
	{
		AtomicInteger count = getCount(key);
		
		return count.incrementAndGet();
	}
	
	public int get(T key)
	{
		AtomicInteger count = getCount(key);
		
		return count.get();
	}

	private AtomicInteger getCount(T key) {
		ConcurrentHashMap<T, AtomicInteger> counter = mCounts.get();
		
		AtomicInteger count = counter.get(key);
		if(count==null) {
			counter.putIfAbsent(key, new AtomicInteger());
			count = counter.get(key);
		}
		
		return count;
	}
}
