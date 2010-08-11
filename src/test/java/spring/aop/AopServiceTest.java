package spring.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import spring.beans.ServiceObjects;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/spring/aop/aop_service.xml"})
public class AopServiceTest
{
	private static final Log mLog = LogFactory.getLog(AopServiceTest.class);
	
	@Autowired
	@Qualifier("txMethodService")
	ServiceObjects.TxMethodService txMethodService;
	
	@Test
	public void testTxMethod()
	{
		txMethodService.getReadOnly();
		txMethodService.getTransactional();
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
