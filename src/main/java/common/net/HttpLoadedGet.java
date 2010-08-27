package common.net;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 * allow us ot 
 */
public class HttpLoadedGet extends HttpEntityEnclosingRequestBase
{
    public final static String METHOD_NAME = "GET";
    
    public HttpLoadedGet() {
        super();
    }

    public HttpLoadedGet(final URI uri) {
        super();
        setURI(uri);
    }

    /**
     * @throws IllegalArgumentException if the uri is invalid. 
     */
    public HttpLoadedGet(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
