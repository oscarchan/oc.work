package apache.http.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.junit.Test;

import common.ProxyUtils;

public class HttpClientTest
{
	private static final Log log = LogFactory.getLog(HttpClientTest.class);
	private static final String EMAIL_URL = "http://feature8-secure.auto.zlive.zynga.com/check_email.php";
	private static final String EMAIL_PARAM = "email";
	
	@Test
	public void testHttpClient() throws ClientProtocolException, IOException 
	{
		HttpClient httpclient = new DefaultHttpClient();

		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(EMAIL_PARAM, "oscarchan@gmail.com"));
		String query = URLEncodedUtils.format(parameters, "UTF-8");
		
		HttpGet httpget = new HttpGet(EMAIL_URL + "?" + query);
		
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        
        log.info("mesg=" + responseBody);
	}
	
	/**
	 * test how 
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testPooledConnections() throws ClientProtocolException, IOException, InterruptedException, ExecutionException 
	{
		// --- set up ---
		BasicHttpParams httpParams = new BasicHttpParams();
		
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		ClientConnectionManager manager = ProxyUtils.getLoggerProxy(new ThreadSafeClientConnManager(httpParams, registry), ClientConnectionManager.class);
		
		final DefaultHttpClient httpclient = new DefaultHttpClient(manager, httpParams);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(EMAIL_PARAM, "oscarchan@gmail.com"));
		String query = URLEncodedUtils.format(parameters, "UTF-8");
		
		final HttpGet httpget = new HttpGet(EMAIL_URL + "?" + query);
        final ResponseHandler<String> responseHandler = new BasicResponseHandler();
        
		// --- execution ---
        ExecutorService pool = Executors.newCachedThreadPool();
        
        List<Future<String>> futures = new ArrayList<Future<String>>();
        for(int i=0;i<10;i++) {
        	final int index = i;
        	Future<String> submitted = pool.submit(new Callable<String>()
			{
				public String call() throws Exception
                {
					log.info("start: " + index);
					String responseBody = httpclient.execute(httpget, responseHandler);

					log.info("end: " + index);
	                return responseBody;
                }
			});
        	
        	futures.add(submitted);
        }
        
        
        for (Future<String> future : futures) {
	        String responseBody = future.get();
        	
	        log.info("mesg=" + responseBody);
        }
	}
	
	@Test
	public void testHttpPost() throws UnsupportedEncodingException 
	{
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair(EMAIL_PARAM, "value1"));

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
		        "UTF-8");
		HttpPost httppost = new HttpPost("http://localhost/handler.do");
		httppost.setEntity(entity);
	}
}
