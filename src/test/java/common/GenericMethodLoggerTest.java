package common;

import java.util.Collections;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import spring.context.AnnotationConfigContextLoader;
import ws.rs.RestSampleService;

import common.time.TimeSources.AdjustableTimeSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, value="common.GenericMethodLoggerTest")
//@ContextConfiguration(locations={"/common/GenericMethodLoggerTest.xml"})
@Configuration
public class GenericMethodLoggerTest
{
    private static Logger log = LoggerFactory.getLogger(GenericMethodLoggerTest.class);

    private AdjustableTimeSource timeSource = new AdjustableTimeSource();
    
	@Autowired @Qualifier("mockedLogger")
	private Logger mockedLogger;
    
    @Autowired @Qualifier("restSampleService")
    private RestSampleService sampleService;
	
	@Test
	public void testSampleService() throws Throwable
	{
		SamplePojo sample = new SamplePojo();
		sample.setId("oscar");
		sample.setName("chan");

		Set<SamplePojo> samples = Collections.singleton(sample);
		
		
    	EasyMock.reset(mockedLogger);
		mockedLogger.info("setSamples(..): returned=" + samples);
		EasyMock.replay(mockedLogger);
		
		sampleService.setSamples(samples);
		
		EasyMock.verify(mockedLogger);
	}
	
//	@Bean  /** same as adding <aop:aspectj-autoproxy />*/
//	public AnnotationAwareAspectJAutoProxyCreator setUpAopScan()
//	{
//		AnnotationAwareAspectJAutoProxyCreator creator = new AnnotationAwareAspectJAutoProxyCreator();
//		
//		creator.setProxyTargetClass(true);
//		
//		return creator;
//	}
//	
	@Bean(name="mockedLogger")
	public Logger setUpMockedLogger()
	{
		return EasyMock.createMock(Logger.class);
	}
	
	@Bean(name="methodLogger")
	public GenericMethodLogger getMethodLogger()
	{
		return new TestLogger();
	}

	@Bean(name="restSampleService")
	public RestSampleService setUpRestSampleService()
	{
		return new RestSampleService();
	}

	@Aspect
	public static class TestLogger extends GenericMethodLogger
	{
		@Autowired @Qualifier("mockedLogger")
		private Logger mockedLogger;

		@Around("within (ws..*RestSampleService) ")
		public Object logTx(ProceedingJoinPoint jp) throws Throwable
		{
			return super.logTx(jp);
		}
		
		@Override
		protected Logger getLogger(ProceedingJoinPoint jp)
		{
		    return mockedLogger;
		}
		
	}
}
