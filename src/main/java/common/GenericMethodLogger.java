package common;


import java.util.concurrent.atomic.AtomicInteger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.time.TimeSource;
import common.time.TimeSources;

/**
 * the default method logger, which can handle some logging concerns in most cases
 */
public class GenericMethodLogger
{
	private TimeSource timeSource = TimeSources.SYSTEM_TIME_SOURCE;
	
	/** log method name if set */
	private boolean methodLoggable=true;
	/** log parameters if set */
	private boolean parameterLoggable=false;
	/** log returned object or exception if set */ 
	private boolean resultLoggable=true; 
	/** log elapsed time if set */
	private boolean txTimeLoggable=true;
	
	/** log only if elapsed time of method execution exceeds the given threshold in msec */
	private long logTxTimeThreshold = 0; 
	
	/** 
	 * tracking number of calls of this instance by AOP to avoid multiple logging</br> 
	 * NOTE: as Spring AOP does not support CFLOW and CFLOWBELOW, we need to implement our own nested count, so avoid 
	 */
	private EnhancedThreadLocal<AtomicInteger> nestedCounter = new EnhancedThreadLocal<AtomicInteger>()
	{
		protected AtomicInteger initialValue() { return new AtomicInteger(); }
	};
	
	public void setTimeSource(TimeSource timeSource)
    {
	    this.timeSource = timeSource;
    }
	
	public void setMethodLoggable(boolean methodLoggable)
    {
	    this.methodLoggable = methodLoggable;
    }
	
	public void setParameterLoggable(boolean parameterLoggable)
    {
	    this.parameterLoggable = parameterLoggable;
    }
	
	public void setTxTimeLoggable(boolean txTimeLoggable)
    {
	    this.txTimeLoggable = txTimeLoggable;
    }
	
	public void setLogTxTimeThreshold(long txTimeThreshold)
    {
	    this.logTxTimeThreshold = txTimeThreshold;
    }
	
	// TODO extract these append methods as a style class instead
	protected StringBuilder appendMethodSignature(StringBuilder sb, JoinPoint jp, boolean parameterIncluded)
	{
		StringBuilder methodSignature = ReflectionUtils.getMethodSignature(jp, parameterIncluded);
		
		return sb.append(methodSignature);
	}
	
	protected StringBuilder appendReturned(StringBuilder sb, Object result)
	{
		return sb.append(": returned=").append(result);
	}
	
	protected StringBuilder appendThrowable(StringBuilder sb, Throwable e)
	{
		return sb.append(": throwable=").append(e);
	}
	
	protected StringBuilder appendTiming(StringBuilder sb, long elapsedTime)
	{
		return sb.append(": txTime=").append(elapsedTime).append(" ms");
	}
	
	protected StringBuilder appendRemaining(StringBuilder sb)
	{
		return sb; 
	}
	
	protected Logger getLogger(ProceedingJoinPoint jp)
    {
	    Logger log = LoggerFactory.getLogger(jp.getSignature().getDeclaringType());
	    
	    return log;
    }
	
	public int getNestedCount()
	{
		if(nestedCounter.exists()==false)
			return 0;
			
		return nestedCounter.get().get();
	}
	
	public Object logTx(ProceedingJoinPoint jp) throws Throwable
	{
		Logger log = getLogger(jp);
		
		long startTime = timeSource.currentTimeMillis();
		
		StringBuilder sb = new StringBuilder();

		nestedCounter.get().incrementAndGet();
		
		try {
			Object returned = jp.proceed();
			
			if(resultLoggable)
				appendReturned(sb, returned);
			
			return returned;
		} catch (Throwable e) {
			
			if(resultLoggable)
				appendThrowable(sb, e);
			
			// continue with existing codes
			throw e;
			
		} finally {			
			long endTime = timeSource.currentTimeMillis();
			long txTime = endTime - startTime;
			
			int nestedLevel = nestedCounter.get().decrementAndGet();
			
			if(nestedLevel<=0) // remove if reaching top level
				nestedCounter.remove();
			
			if(nestedLevel<=0 && txTime >= logTxTimeThreshold) { //log only at the top level and tx time exceeds the threshold 
				

				if(methodLoggable) {
					// delay appending the method signature until we are certain that we will log this.
					// switch buffer with a new one because we are adding a prefix
					sb = appendMethodSignature(new StringBuilder(), jp, parameterLoggable)
						.append(sb);
				}
				
				if(txTimeLoggable)
					appendTiming(sb, txTime);

				// for subclass to add more detail
				appendRemaining(sb);
				
				log.info(sb.toString());
			}
			
		}
	}

	// TODO LOW ochan make possible to check if value exists before accessing it in ThreadLocal 
	static class EnhancedThreadLocal<T>
	{
		ThreadLocal<T> threadlocal = new ThreadLocal<T>();
		
	    public T get()
	    {
	    	T local = threadlocal.get();
	    	if(local==null) { 
	    		local = initialValue();
	    		threadlocal.set(local);
	    	}
	    	
	    	return local;
	    		
	    }

	    public boolean exists()
		{
			T local = threadlocal.get();
			
			return local!=null;
		}
		
	    protected T initialValue() 
	    {
	        return null;
	    }		
	    
	    public void remove()
	    {
	    	threadlocal.remove();
	    }
	    
	}
}
