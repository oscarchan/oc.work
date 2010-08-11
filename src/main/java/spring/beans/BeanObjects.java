package spring.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class BeanObjects
{
	private static final Log mLog = LogFactory.getLog(BeanObjects.class);
	

	/** bean with a simple name property */
	public static class SampleBean implements BeanNameAware
	{
		private String name;
		
		/**
		 * component annotation requires annotation processor
            <context:annotation-config/>
            
            <context:component-scan base-package="com.xxx.mypackage" />            		 
		 */
		@PostConstruct 
		public void init()
		{
			mLog.info("init(" + name + ")");
		}
		
		public String getName()
		{
			return name;
		}

		public <T> T returnObject(T object)
		{
			return object;
		}
		
		public void throwException(Throwable e) throws Throwable
		{
			throw e;
		}
		
		public void setName(String name)
		{
			this.name = name;
		}

		/** component annotation requires annotation processor */
		@PreDestroy
		public void destory()
		{
			mLog.info("destory(" + name + ")");
		}
		
		public String toString()
		{
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append(name)
				.toString();
		}

		public void setBeanName(String name)
		{
			this.name = name;
		}
	}
	
	/** instantiate to register sample bean's name */
	public static class SampleBeanPostBeanFactory implements BeanFactoryPostProcessor
	{
		public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
		{
			Map<String, SampleBean> beans = beanFactory.getBeansOfType(SampleBean.class);
			
			for (String beanName : beans.keySet()) {
				SampleBean sampleBean = beans.get(beanName);
				sampleBean.setName(beanName);
			}
		}
	}
	/** bean with Properties */
	public static class PropertiesBean
	{
		private Properties properties = new Properties();

		public PropertiesBean()
		{
			// NOTE: the following will be ignored.
			properties.setProperty("init", "original");
		}
		
		public Properties getProperties()
		{
			return properties;
		}

		public void setProperties(Properties properties)
		{
			this.properties = properties;
		}

		public String toString()
		{
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}

	
	@Deprecated
	public static class ObjectReporter
	{
		private static final Log mLog = LogFactory.getLog(ObjectReporter.class);
		
		private Object object;
		
		public void setObject(Object object)
		{
//			mLog.info("class=" + object.getClass() + ": id=" + object);
			this.object = object;
		}

		public String toString()
		{
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}
	
	public static class StaticFactoryInstantiator
	{
		public static ConstructorInstantiatedBean instance()
		{
			return new ConstructorInstantiatedBean("StaticFactoryInstance", "test static factory method");
		}
	}
	
	public static class FactoryInstantiator
	{
		public ConstructorInstantiatedBean instance()
		{
			return new ConstructorInstantiatedBean("FactoryInstance", "test non-static factory method");
		}
	}
	
	public static class ConstructorInstantiatedBean
	{
		private static final Log mLog = LogFactory.getLog(ConstructorInstantiatedBean.class);
		private String name;
		private int id;
		private String description;
		
		public ConstructorInstantiatedBean(String name)
		{
			this(name, name);
		}
		public ConstructorInstantiatedBean(String name, String description)
		{
			mLog.info("name=" + name + ", desc=" + description);
			
			this.name = name;
			this.description = description;
		}
		
		public ConstructorInstantiatedBean(int id, String name)
		{
			mLog.info("id=" + id + ", name" + name);
			
			this.id = id;
			this.name = name;
		}
		
		public String toString()
		{
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("name", name)
				.append("id", id)
				.append("desc", description)
				.toString();
		}
	}
	
	public static class CompoundParent
	{
		private CompoundChild child = new CompoundChild();
		private List<CompoundChild> childList = new ArrayList<CompoundChild>();
		private Map<String, CompoundChild> childMap = new HashMap<String, CompoundChild>();
		
		public CompoundParent()
		{
			for(int i=0;i<10;i++) {
				childList.add(new CompoundChild());
			}
			
			childMap.put("a", new CompoundChild());
		}
		public CompoundChild getChild()
		{
			return child;
		}

		public void setChild(CompoundChild child)
		{
			this.child = child;
		}
		public List<CompoundChild> getChildList()
		{
			return childList;
		}
		
		public void setChildList(List<CompoundChild> childList)
		{
			this.childList = childList;
		}
		
		
		public Map<String, CompoundChild> getChildMap()
		{
			return childMap;
		}
		public void setChildMap(Map<String, CompoundChild> childMap)
		{
			this.childMap = childMap;
		}
		public String toString()
		{
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append(child)
				.append("list", childList)
				.append("map", childMap)
				.toString()
				;
		}
	}
	
	public static class CompoundChild
	{
		private String name;

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}
		
		public String toString()
		{
			return "Child[" + name + "]";
		}
	}
	
	public static class AutoWiringComponent1
	{
		private String name;

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}
		
		public String toString()
		{
			return "AutoWiring1[" + name + "]";
		}
	}
	
	public static class AutoWiringComponent2
	{
		private String name;

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}
		
		public String toString()
		{
			return "AutoWiring2[" + name + "]";
		}
	}

	public static class AutoWiringClient
	{
		private AutoWiringComponent1 component1;
		private AutoWiringComponent2 component2;
		
		public AutoWiringComponent1 getComponent1()
		{
			return component1;
		}
		public void setComponent1(AutoWiringComponent1 component1)
		{
			this.component1 = component1;
		}
		public AutoWiringComponent2 getComponent2()
		{
			return component2;
		}
		public void setComponent2(AutoWiringComponent2 component2)
		{
			this.component2 = component2;
		}
		
		public String toString()
		{
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}
	
	public static class AutoWiringComponent3
	{
		private String name;

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}
		
		public String toString()
		{
			return "AutoWiring3[" + name + "]";
		}
	}
	
	public static class AutoWiringClient3
	{
		private AutoWiringComponent3 component3;

		public AutoWiringComponent3 getComponent3()
		{
			return component3;
		}

		public void setComponent3(AutoWiringComponent3 component3)
		{
			this.component3 = component3;
		}
		
		public String toString()
		{
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}
	
	public static class FactoryAwareBean implements BeanFactoryAware
	{
		private static final Log mLog = LogFactory.getLog(FactoryAwareBean.class);
		
		public void setBeanFactory(BeanFactory beanFactory) throws BeansException
		{
			mLog.info("beanFactoryAware: " + beanFactory);
		}
		
	}
	public static void main(String[] args) throws InterruptedException
	{
		
		Resource[] resources = new Resource[] { 
				new ClassPathResource("/spring/beans/factory/samples.xml"),
				new ClassPathResource("/spring/beans/factory/construction.xml"),
				new ClassPathResource("/spring/beans/factory/collections.xml"),
				new ClassPathResource("/spring/beans/factory/autowiring.xml")
		};
		
//		ClassPathXmlApplicationContext rootFactory = new ClassPathXmlApplicationContext(new String[] {
//					"/spring/beans/factory/samples.xml",
//					"/spring/beans/factory/construction.xml",
//					"/spring/beans/factory/collections.xml",
//					"/spring/beans/factory/autowiring.xml"
//				}
//			);
//		ConfigurableListableBeanFactory factory = rootFactory.getBeanFactory();
		
		XmlBeanFactory rootFactory = new XmlBeanFactory(new ClassPathResource("/spring/beans/factory/root.xml"));
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory(rootFactory);
		
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		
		reader.loadBeanDefinitions(resources);
		
		mLog.info("samples1=" + rootFactory.getBean("rootSample1"));
		mLog.info("samples1=" + rootFactory.getBean("rootSample1"));
		
		/*
		mLog.info("starting");
		for (String name : factory.getBeanDefinitionNames()) {
			BeanDefinition definition = factory.getBeanDefinition(name);
			
			mLog.info("retrieving bean name: " + (StringUtils.isBlank(name)?"unknown":name) + ": type=" + ClassUtils.getShortClassName(definition.getBeanClassName()));
			
			try {
				Object bean = factory.getBean(name);
			
				mLog.info("retrieved: " + bean);
			} catch (BeansException e) {
				mLog.info("failed to retrieve bean: " + name, e);
			}
			
		}
		
		mLog.info("cleaning up");
//		factory.destroyBean("beanInitDestoryCycle", bean);

		*/
	}
}
