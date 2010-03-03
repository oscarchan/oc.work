package sandbox.algorithm;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;


public class PowerFuncRecurrencePerformanceTest 
{
	private static final int BASE_FACTOR = 2;

	private static Log mLog = LogFactory.getLog(PowerFuncRecurrencePerformanceTest.class);
	
	ThreadLocal<AtomicInteger> mCost= new ThreadLocal<AtomicInteger>()
	{
		protected AtomicInteger initialValue() {
			return new AtomicInteger();
		};
	};	
	
	@Test
	public void testRecursivePower()
	{
		for (int n = 0; n < 100; n++) {
//			test_not_optimized(n);
			test_half_optimized(n);
		}
	}

	private void test_not_optimized(int n) {
		double result = power_not_optimized(BASE_FACTOR, n);
		int cost = mCost.get().getAndSet(0);
		
		mLog.info("power w/o optimized: n=" + n + ": cost=" + cost + ": result=" + result);
	}
	
	private void test_half_optimized(int n) {
		double result = power_half_optimized(BASE_FACTOR, n);
		int cost = mCost.get().getAndSet(0);
		
		mLog.info("power w/o half optimized: n=" + n + ": cost=" + cost + ": result=" + result);
	}

	// this is O(N)!! really!?
	private double power_not_optimized(long x, long y)
	{
		// fix cost to 1
		mCost.get().incrementAndGet();
		
		if(y==0)
			return 1.0;
		else if(y==1)
			return (double) x;
		else if(y%2==0)
			return power_not_optimized(x, y / 2) * power_not_optimized(x, y / 2);
		else
			return power_not_optimized(x, y/ 2) * power_not_optimized(x, y / 2 + 1);
	}
	
	private double power_half_optimized(long x, long y)
	{
		// fix cost to 1
		mCost.get().incrementAndGet();
		
		if(y==0)
			return 1.0;
		
		if(y==1)
			return (double) x;

	
		double value = power_half_optimized(x, y / 2);
		value *= value;
		
		if(y%2==1)
			value *= x;
		
		return value;
	}
	
}
