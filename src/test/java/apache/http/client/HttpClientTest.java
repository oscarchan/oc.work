package apache.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

public class HttpClientTest
{
	private static final String EMAIL_URL = "http://feature8-secure.auto.zlive.zynga.com/check_email.php";
	private static final String EMAIL_PARAM = "email";
	
	@Test
	public void testHttpClient() throws ClientProtocolException, IOException 
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(EMAIL_URL);

		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(EMAIL_PARAM, "oscarchan@gmail.com"));
		URLEncodedUtils.format(parameters, "UTF-8");
		
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			byte[] tmp = new byte[2048];
			StringBuffer sb = new StringBuffer();
			for(char ch = (char) instream.read(tmp); ch != -1; ch = (char) instream.read(tmp)) {
				sb.append(ch);
			}
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
