package spring.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

public class AopAdvices
{
	/**
	 * NOTE: MethodBeforeAdvice only work with aop:advisor
	 * 
          <bean id="methodLogger" class="spring.aop.AopAdvices$LogAdvice" />

	      <aop:advisor advice-ref="methodBeforeLogger"
              pointcut="execution(* spring.beans.BeanObjects$SamplePojo+.set*Name(..) )" />
       OR
	 
	      <aop:
	 * @author ochan
	 */
	public static class LogBeforeAdvice implements MethodBeforeAdvice
	{
		public void before(Method method, Object[] args, Object target) throws Throwable
		{
			Log log = LogFactory.getLog(target.getClass());
			
			logMethod(AdviceType.before, log, method, args, null, "LogBeforeAdvice");
		}
	}
	
	public static class LogAdvice implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice
	{
	    public void before(JoinPoint jp)
	    {
            logMethod(AdviceType.before, jp, null, "LogAdvice");
	    }
	    
		public void before(Method method, Object[] args, Object target) throws Throwable
		{
			Log log = LogFactory.getLog(target.getClass());
			logMethod(AdviceType.before, log, method, args, null, "LogAdvice");
		}

		public void afterReturning(Object returned, Method method, Object[] args, Object target) throws Throwable
		{
			Log log = LogFactory.getLog(target.getClass());
			logMethod(AdviceType.afterReturning, log, method, args, returned, "LogAdvice");
		}
		
		public void afterThrowing(Method method, Object[] args, Object target, Throwable throwable)
		{
			Log log = LogFactory.getLog(target.getClass());
			logMethod(AdviceType.afterThrowing, log, method, args, throwable, "LogAdvice");
		}
		
		public void after(JoinPoint jp)
        {
            logMethod(AdviceType.after, jp, null, "LogAdvice");
        }
		
		
		public void around(ProceedingJoinPoint joinPoint) throws Throwable
		{
			
			Object returned = null;
 			try {
 			    returned = joinPoint.proceed();
 			} finally {
 			    logMethod(AdviceType.around, joinPoint, returned, "LogAdvice");
 			}
 			
		}
	}
	
	/**
	 * tracking the time spent on executing join points, regardless the end state of join point executions succeed without exceptions 
	 */
	public static class TimingLogAdvice implements MethodInterceptor
	{
		private int mLogThreshold = 0; // log if response time is over this threashold
		
		public Object invoke(MethodInvocation invocation) throws Throwable
		{
			Log log = LogFactory.getLog(invocation.getThis().getClass());
			
			long start = System.currentTimeMillis();
			

			try {
				Object returned = invocation.proceed();
				
				long duration = System.currentTimeMillis() - start;
				logMethod(log, invocation, returned, "took " + duration + " ms");
				
				return returned;
			} catch (Throwable e) {

				long duration = System.currentTimeMillis() - start;
				logMethod(log, invocation, e, "took " + duration + " ms");
				
				throw e;
			}
		}
		
	}
	
	/**
	 * replace an argument if it matches the replacementMap 
	 */
	public static class ArgReplacementAdvice 
	{
	    public Map<Object, Object> mReplacementMap = new HashMap<Object, Object>();
	    
	    public ArgReplacementAdvice(Map<Object, Object> replacementMap)
	    {
	        mReplacementMap.putAll(replacementMap);
	    }
	    
        public void replace(ProceedingJoinPoint jp) throws Throwable
        {
            ArrayList<Object> replacedArgs = new ArrayList<Object>();
            
            boolean replaced = false;
            
            for (Object arg : jp.getArgs()) {
                Object replacedArg = arg;
                
                if(mReplacementMap.containsKey(replacedArg)) {
                    replacedArg = mReplacementMap.get(arg);
                    replaced = true;
                }
                    
                replacedArgs.add(replacedArg);
            }
            
            if(replaced)
                jp.proceed(replacedArgs.toArray());
            else
                jp.proceed();
            
        }
	}


	private static void logMethod(AdviceType type, JoinPoint joinPoint, Object returned, String message) 
	{
		Log log = LogFactory.getLog(joinPoint.getThis().getClass());
		
		logMethod(type, log, joinPoint.getSignature().getName(), joinPoint.getArgs(), returned, message);

	}
	private static void logMethod(Log log, MethodInvocation invocation, Object returned, String message)
	{
		Object[] args = invocation.getArguments();
		Method method = invocation.getMethod();
		
		logMethod(AdviceType.around, log, method, args, returned, message);
	}
	private static void logMethod(AdviceType type, Log log, Method method, Object[] args, Object returned)
	{
		logMethod(type, log, method, args, returned, null);
	}
	
	private static void logMethod(AdviceType type, Log log, Method method, Object[] args, Object returned, String logMessage)
	{
		logMethod(type, log, method.getName(), args, returned, logMessage);
	}
	
	private static void logMethod(AdviceType type, Log log, String signature, Object[] args, Object returned, String logMessage)
	{
		if(log.isInfoEnabled()) {
			
			StringBuffer msg = 
				new StringBuffer();
			
			msg
				.append("aop-")
				.append(type.getCode())
				.append(": ")
				.append(signature)
				.append("(")
			;

			for(Iterator<Object> itr=Arrays.asList(args).iterator();itr.hasNext();) {
				msg.append(itr.next());
				if(itr.hasNext())
					msg.append(", ");
			}
			
			msg.append(")");
				
			
			if(returned!=null) {
				switch(type)
				{
					case afterReturning:
					{
						msg.append("=").append(returned);
						break;
					}
					case afterThrowing:
					{
						msg.append(" threw ").append(returned);
						break;
					}
					case around:
					{
						if(returned instanceof Throwable)
							msg.append(" threw ").append(returned);
						else
							msg.append("=").append(returned);
						break;
					}
				}
			}
			
			if(logMessage!=null)
			    msg.append(": ").append(logMessage);
			
			log.info(msg);
		}
	}
	
	enum AdviceType
	{
		before("before"),
		afterReturning("after-return"),
		afterThrowing("after-throw"),
		around("around"),
		after("after")
		;
		
		private String mCode;
		
		private AdviceType(String code)
		{
			mCode = code;
		}
		
		public String getCode()
		{
			return mCode;
		}
	}
	
}
