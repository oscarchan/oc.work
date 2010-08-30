package common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ProxyUtils
{

	/**
	 * logging all method of the object until an interface
	 * @param object
	 * @param interfaceClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
  public static <I, T extends I> I getLoggerProxy(T object, Class<? extends I> interfaceClass)
	{
		return (I) Proxy.newProxyInstance(object.getClass().getClassLoader(), new Class[] { interfaceClass} , new LoggingHandler(object));
	}
	
	/**
	 * instead of using existing thread, use concurrent executor to execute a method call instead
	 * @param object
	 * @param interfaceClass
	 * @param executor
	 * @return
	 */
	@SuppressWarnings("unchecked")
  public static <I, T extends I> T getDispatcherProxy(T object, Class<? extends I> interfaceClass, Executor executor)
	{
		p_validateInterface(interfaceClass);

		return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(), new Class[] {interfaceClass}, new DispatcherHandler(object, executor));
	}


	@SuppressWarnings("unchecked")
  public static <I, T extends I> T getCompositeProxy(Class<? extends I> interfaceClass, Collection<? extends T> set)
	{
		p_validateInterface(interfaceClass);
		
		return (T) Proxy.newProxyInstance(set.getClass().getClassLoader(), new Class[] { interfaceClass } , new CompositeHandler(set, false));
	}
	
	@SuppressWarnings("unchecked")
  public static <I, T extends I> T getCompositeProxy(Class<? extends I> interfaceClass, T ... composites)
	{
		p_validateInterface(interfaceClass);
		
		return (T) Proxy.newProxyInstance(composites.getClass().getClassLoader(), new Class[] { interfaceClass } , new CompositeHandler(Arrays.asList(composites), false));
	}
	
//	/**
//	 * 
//	 * @param <I> interface to be proxy
//	 * @param <T> target object's type
//	 * @param object target object
//	 * @param interfaceClass one of the instances of target object 
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//  public static <I, T extends I> I getInvocationTrackProxy(T object, Class<? extends I> interfaceClass)
//	{
//		return (I) Proxy.newProxyInstance(object.getClass().getClassLoader(), new Class[] { interfaceClass }, new InvocationTracker(object, interfaceClass));
//	}
	
	private static void p_validateInterface(Class interfaceClass)
	{
		Method[] methods = interfaceClass.getMethods();
		
		for (Method method : methods) {
			Class<?> returnType = method.getReturnType();
			
			if(Void.TYPE.equals(returnType)==false)
				throw new IllegalArgumentException("unable to dispatch a method with return value: method=" + method);
		}
	}
	
	
	public static class LoggingHandler<T> implements InvocationHandler
	{
		private T mWrappedObj;
		
		public LoggingHandler(T wrappedObj)
		{
			if(wrappedObj==null)
				throw new IllegalArgumentException("missing object");
			
			mWrappedObj = wrappedObj;
		}
		
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
		{
			Log log = LogFactory.getLog(mWrappedObj.getClass());
			
			if(log.isInfoEnabled()) { 
				StringBuffer sb = new StringBuffer(method.getName());
				
				sb.append("(");
				
				for (int i = 0; args!=null && i < args.length; i++) {
					if(i!=0)
						sb.append(", ");
					
					sb.append(args[i]);
				}
				
				sb.append(")");
				
				log.info(sb.toString());
			}					
				
			return method.invoke(mWrappedObj, args);
		}
	}

	public static class DispatcherHandler<T> implements InvocationHandler
	{
		private T mWrappedObj;
		
		public DispatcherHandler(T obj, Executor executor)
		{
			if(obj==null)
				throw new IllegalArgumentException("missing object");
			
			mWrappedObj = obj;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
		{
			
			return method.invoke(mWrappedObj, args);
		}
	}

	public static class CompositeHandler<T> implements InvocationHandler
	{
		private Collection<? extends T> mCompositeSet;
		private boolean mTerminateOnError;
		
		public CompositeHandler(Collection<? extends T> composite, boolean terminateOnError)
		{
			mCompositeSet = composite;
			
			mTerminateOnError = terminateOnError;
		}
		
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
		{
			for (Object object : mCompositeSet) {
				
				if(mTerminateOnError) {
					method.invoke(object, args);
				} else {
					try {
						method.invoke(object, args);
					} catch (Exception e) { 
						Log log = LogFactory.getLog(object.getClass());
						
						log.warn("DispatcherHandler unable to execute: obj=" + object + ", method=" + method );
					}
				}
				
			}
			
			return null;
		}
	}
	
//	public static class InvocationTracker<T> implements InvocationHandler
//	{
//		private static final Log mLog = LogFactory.getLog(InvocationTracker.class);
//
//
//		private static final long REPORTING_INTERVAL_MS = 1 * 60 * 1000;
//		
//		private static Timer mReporter = new Timer();
//		
//		private PerfDataCollector mTracker = new PerfDataCollector();
//		
//		private long mLastInvocationTime = System.currentTimeMillis();
//		
//		private T mWrappedObj;
//		private Class<T> mInterfaceClass;
//		
//		public InvocationTracker(T wrappedObj, Class<T> interfaceClass)
//		{
//			if(wrappedObj==null)
//				throw new IllegalArgumentException("missing object");
//			
//			mWrappedObj = wrappedObj;
//			mInterfaceClass = interfaceClass;
//			
//			mReporter.schedule(new TimerTask() {
//
//				@Override
//				public void run()
//				{
//					long currTime = System.currentTimeMillis();
//					
//					if(currTime - mLastInvocationTime < REPORTING_INTERVAL_MS) // if already reported before
//						return;
//						
//					for (String display : mTracker.getDisplay()) {
//						mLog.info(mInterfaceClass.getSimpleName() + ": " + display);
//					}
//					
//				}}, REPORTING_INTERVAL_MS, REPORTING_INTERVAL_MS);
//		
//		}
//		
//		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
//		{
//			long start = System.currentTimeMillis();
//			
//			mLastInvocationTime = start;		// update the last invocation 
//
//			try {
//				return method.invoke(mWrappedObj, args);
//			} finally {
//				long end = System.currentTimeMillis();
//				
//				long delta = end - start;
//				
//				mTracker.incTrans(new MethodId(method), (int) delta);
//			}
//		}
//		
//		static class MethodId implements TransId
//		{
//			private Method mMethod;
//			
//			public MethodId(Method method)
//			{
//				mMethod = method;
//			}
//			
//			public String getMetricsName()
//			{
//				return mMethod.toGenericString();
//			}
//			
//			public String toString()
//			{
//				return mMethod.toGenericString();
//			}
//			
//			public int hashCode()
//			{
//				return mMethod.hashCode();
//			}
//			
//			public boolean equals(Object obj)
//			{
//				if(!(obj instanceof MethodId))
//					return false;
//					
//				MethodId id = (MethodId) obj;
//				
//				return mMethod.equals(id.mMethod);
//			}
//		}
//	}
}
