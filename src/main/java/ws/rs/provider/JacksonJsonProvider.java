package ws.rs.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

public class JacksonJsonProvider
	implements MessageBodyReader<Object>, MessageBodyWriter<Object>	
{

	public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType)
    {
	    // TODO Auto-generated method stub
	    return false;
    }

	public long getSize(Object t, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType)
    {
	    // TODO Auto-generated method stub
	    return 0;
    }

	public void writeTo(Object t, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException,
            WebApplicationException
    {
	    // TODO Auto-generated method stub
	    
    }

	public boolean isReadable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType)
    {
	    // TODO Auto-generated method stub
	    return false;
    }

	public Object readFrom(Class<Object> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException
    {
	    // TODO Auto-generated method stub
	    return null;
    }

}
