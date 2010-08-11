package spring.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import spring.beans.BeanObjects;


/**
 * - cannot get BeanFactoryPostProcessor and aop:config working together, but BeanNameAware is okay (need more debugging)
 * 
 * 
 * @author ochan
 */
public class AopTest
{
	private static final Log mLog = LogFactory.getLog(AopTest.class);

	/**
	 * Test how point cut can apply to different interfaces
	 */
	@Test
	public void testAopAdvice()
	{
	    ListableBeanFactory mBeanFactory = new ClassPathXmlApplicationContext("/spring/aop/aop.xml");
		Object object = mBeanFactory.getBean("sampleBean_pointCut_before");
		
		mLog.info("object = " + object);
		BeanObjects.SampleBean sample = (BeanObjects.SampleBean) object; 
		
		sample.init();
		sample.getClass();
		sample.returnObject("A");
		sample.setName("name");
		sample.getName();
		try {
			sample.throwException(new Exception("exception"));
		} catch (Throwable e) {
		}
	}
	
	public void testAnnotation()
	{
	    ListableBeanFactory mBeanFactory = new ClassPathXmlApplicationContext("/spring/aop/aop.xml");
		Object object = mBeanFactory.getBean("sampleBean_pointCut_before");
		
		mLog.info("object = " + object);
		BeanObjects.SampleBean sample = (BeanObjects.SampleBean) object; 
		
		sample.init();
		sample.getClass();
		sample.returnObject("A");
		sample.setName("name");
		sample.getName();
		try {
			sample.throwException(new Exception("exception"));
		} catch (Throwable e) {
		}		
	}

	
}
