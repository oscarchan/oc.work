package ws.rs.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.provider.JSONProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

@Provider
@Produces("text/json")
public class XStreamJsonProvider extends JSONProvider 
{
	private static Class<? extends HierarchicalStreamDriver> driverClass = JsonHierarchicalStreamDriver.class;
	public static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss zzz";

    private static final Logger logger = LoggerFactory.getLogger(XStreamJsonProvider.class);
    
	public Object readFrom(Class<Object> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException 
	{
		throw new UnsupportedOperationException();
	}
	
    public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException,
            WebApplicationException {
    	
        logger.debug("writing object: {}, type: {}", t, type);
        
        XStream x = new XStream(newJsonDriver());
        x.alias(type.getSimpleName(), type.getClass());
        x.omitField(this.getClass(), "driverClass");
        
        x.registerConverter(new DateConverter(DEFAULT_DATE_PATTERN, new String[0]));
        
        String s = x.toXML(t);

        
        Writer w = new OutputStreamWriter(entityStream, "UTF-8");
        w.write(s);
        w.flush();
    }
    
    private static HierarchicalStreamDriver newJsonDriver() {
    	try {
	        return driverClass.newInstance();
        } catch (InstantiationException e) {
        	logger.error("Unable to instantiate driver: " + driverClass + ". Rolling back to JsonHierarchicalStreamDriver", e);
        	return new JsonHierarchicalStreamDriver();
        } catch (IllegalAccessException e) {
        	logger.error("illegal access when creating driver: " + driverClass + ". Rolling back to JsonHierarchicalStreamDriver", e);
        	return new JsonHierarchicalStreamDriver();
        }
    }
}
