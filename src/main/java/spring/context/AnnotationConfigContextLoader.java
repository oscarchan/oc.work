package spring.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextLoader;

/**
 * a bridge class to connect junit4 and java-based ioc via SpringJUnit4ClassRunner's test context loader
 */
public class AnnotationConfigContextLoader implements ContextLoader
{
	@Override
	public ApplicationContext loadContext(String... locations) throws Exception
	{
        Class<?>[] configClasses = new Class<?>[locations.length];
        for (int i = 0; i < locations.length; i++) {
            configClasses[i] = Class.forName(locations[i]);
        }        
        return new AnnotationConfigApplicationContext(configClasses);
	}

	@Override
	public String[] processLocations(Class<?> clazz, String... locations)
	{
		return locations;
	}
}
