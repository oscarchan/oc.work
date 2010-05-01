package json.simple;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.junit.Test;

public class TestJSONObject
{
  private static Log mLog = LogFactory.getLog(TestJSONObject.class);

  private int _timeOutMsec = 1;
  
  @SuppressWarnings("unchecked")
  @Test
  public void testObjectTypes()
  {   
    JSONObject obj=new JSONObject();
    obj.put("name","foo");
    obj.put("num",new Integer(100));
    obj.put("balance",new Double(1000.21));
    obj.put("sum_as_string", "100");
    obj.put("is_vip",new Boolean(true));
    obj.put("nickname",null);
  
    mLog.info("result: " + obj);
    
  }
}
