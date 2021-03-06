package spring.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import javassist.tools.reflect.Reflection;

import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import common.ReflectionUtils;

import spring.beans.ServiceObjects;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/spring/aop/aop_service.xml"})
public class AopServiceTest
{
	private static final Log mLog = LogFactory.getLog(AopServiceTest.class);
	
	@Autowired
	@Qualifier("txMethodService")
	ServiceObjects.TxMethodService txMethodService;
	
	@Autowired
	@Qualifier("restMemberService")
	ServiceObjects.RestMemberService restService;
	
	@Transactional
	public void testTxMethod()
	{
		txMethodService.getReadOnly();
		txMethodService.getTransactional();
	}
	
	@Test
	public void testRest()
	{
		restService.getMember(233);
		restService.getMemberFriend(123, 456);
		
		try {  restService.throwException(new WebApplicationException(Response.Status.NOT_FOUND)); } catch (WebApplicationException e) { }
		try {  restService.throwException(new WebApplicationException(Response.Status.BAD_REQUEST)); } catch (WebApplicationException e) { }
		try {  restService.throwException(new CustomWebApplicationException()); } catch (WebApplicationException e) { }
	}
	
	static class CustomWebApplicationException extends WebApplicationException { private static final long serialVersionUID = 1L; }
	
	@Aspect
	public static class RestLogAspect
	{
		@Pointcut("within(spring.beans.ServiceObjects)")
		public void classWithServiceObjects() { }
		
		@Pointcut("execution (@javax.ws.rs.GET * *(..) )")
		public void methodGet() { }
		
		@Pointcut("execution (* *(.., @javax.ws.rs.PathParam (*),..) ) ")
		public void methodWithPathParam() { }
		
		/**
		 * name binding to parameter only works in 'target', 'this', and 'args' keywords 
		 */
		// @Pointcut("execution (* *(.., @javax.ws.rs.PathParam (id),..) ) ")
		// public void methodWithPathParam_test(long id ) { }
				
		/**
		 * args (.., @javax.ws.rs.PathParam (id), ..) does not work
		 */
		@Pointcut("execution (* *(..)) && args ( @javax.ws.rs.PathParam (id), ..)")
		public void methodWithFirstPathParam(long id) { }
		
		
		@AfterThrowing(value="classWithServiceObjects()", throwing="e")
		public void logException(JoinPoint jp, WebApplicationException e) throws Throwable
		{
			if(Response.Status.NOT_FOUND.getStatusCode()==e.getResponse().getStatus())
				mLog.info("not found exception thrown: " + e);
			else
				mLog.info("unexpected exception thrown: " + e);
				
		}
		
		@Around("classWithServiceObjects() && methodGet() && methodWithPathParam()") 
		public void logRestMethod(ProceedingJoinPoint jp) throws Throwable
		{
			mLog.info("class & method: " + jp.getSignature().toLongString() );
			
			jp.proceed();
		}
		
		@Around("classWithServiceObjects()") 
		public void logRestMethod_class(ProceedingJoinPoint jp) throws Throwable
		{
			mLog.info("class: " + jp.getSignature().toLongString() );
			
			jp.proceed();
		}
		
		@Around("methodGet()") 
		public void logRestMethod_method(ProceedingJoinPoint jp) throws Throwable
		{
			mLog.info("method: " + jp.getSignature().toLongString() );
			jp.proceed();
		}
		
		@Around("methodWithPathParam()") 
		public void logRestMethod_param(ProceedingJoinPoint jp) throws Throwable
		{
			mLog.info("param: " + jp.getSignature().toLongString() );
			jp.proceed();
		}

		/**
		 * match exactly one long parameter 
		 */
		@Around("methodWithPathParam() && args(id) ") 
		public void logRestMethod_paramOneValue(ProceedingJoinPoint jp, long id) throws Throwable
		{
			mLog.info("param one: " + jp.getSignature().toLongString() + ": id=" + id);
			jp.proceed();
		}

		/**
		 * match first one PathParam parameter
		 */
		@Around("classWithServiceObjects() && methodWithFirstPathParam(id)") 
		public void logRestMethod_paramFirstValue(ProceedingJoinPoint jp, long id) throws Throwable
		{
			mLog.info("param 1st: " + jp.getSignature().toLongString() + ": id=" + id);
			jp.proceed();
		}
		
		@Around("classWithServiceObjects() && methodWithPathParam()") 
		public void logRestMethod_paramAnyValue(ProceedingJoinPoint jp) throws Throwable
		{
			MethodSignature signature = (MethodSignature) jp.getSignature();

			Method method = signature.getMethod();
			
			int argIndex = ReflectionUtils.indexOfAnnotatedParameter(method, PathParam.class);
			
			if(argIndex>=0) {

				Object[] args = jp.getArgs();
				
				mLog.info("param any: " + jp.getSignature().toLongString() + ": id=" + args[argIndex]);

			}

			jp.proceed();
		}				
	}
	@Aspect
	public static class TransactionalLogAspect
	{

 		@Pointcut("execution(@org.springframework.transaction.annotation.Transactional * *(..)) && @annotation(transactional)")
 		public void methodTransactional(Transactional transactional)
 		{
 			
 		}
 		
 		// couldn't figure it out how to work yet!!
 		@Pointcut("@within(org.springframework.transaction.annotation.Transactional) && @annotation(transactional) ")
 		public void classTransactional(Transactional transactional)
 		{
 			
 		}

 		
 		/**
 		 * it is possible to conbine @Pointcut and @Around into one line like below: 
 		 * @Around("execution(@org.springframework.transaction.annotation.Transactional * *(..)) && @annotation(transactional) ")
 		 */
 		
 		
 		
 		@Around("methodTransactional(transactional) || classTransactional(transactional)")
		public void logTxMethod_method(ProceedingJoinPoint jp, Transactional transactional) throws Throwable
		{
			Boolean readOnly = null;
			
			if(transactional!=null)
				readOnly = transactional.readOnly();
			
			mLog.info("logTxMethod_method: " + jp.getSignature().toLongString() + ": readOnly=" + readOnly);
			
			jp.proceed();
		}
	}
}
