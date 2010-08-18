package common;

import java.lang.reflect.Method;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.Assert;

import spring.beans.ServiceObjects;

public class ReflectionUtilsTest
{
	@Test
	public void testIndexOfAnnotatedParameter_none() throws SecurityException, NoSuchMethodException
	{
		
		Method getTransactionMethod = ServiceObjects.TxMethodService.class.getMethod("getTransactional");
		
		int index = ReflectionUtils.indexOfAnnotatedParameter(getTransactionMethod, PathParam.class);
		
		Assert.assertEquals("index", -1, index);
	}
	
	
	@Test
	public void testIndexOfAnnotatedParameter_notFound() throws SecurityException, NoSuchMethodException
	{
		
		Method getMemberFriendMethod = ServiceObjects.RestMemberService.class.getMethod("getMemberFriend", Long.TYPE, Long.TYPE);
		
		int index = ReflectionUtils.indexOfAnnotatedParameter(getMemberFriendMethod, Transactional.class);
		
		Assert.assertEquals("index", -1, index);
	}
	
	
	@Test
	public void testIndexOfAnnotatedParameter_foundOne() throws SecurityException, NoSuchMethodException
	{
		
		Method getMemberFriendMethod = ServiceObjects.RestMemberService.class.getMethod("getMemberFriend", Long.TYPE, Long.TYPE);
		
		int pathParamIndex = ReflectionUtils.indexOfAnnotatedParameter(getMemberFriendMethod, PathParam.class);
		Assert.assertEquals("index", 0, pathParamIndex);
		
		int queryParamIndex = ReflectionUtils.indexOfAnnotatedParameter(getMemberFriendMethod, QueryParam.class);
		Assert.assertEquals("index", 1, queryParamIndex);
		
	}
	
	@Test
	public void testIndexOfAnnotatedParameter_foundFirst() throws SecurityException, NoSuchMethodException
	{
		
		Method getMemberMessageMethod = ServiceObjects.RestMemberService.class.getMethod("getMemberMessage", Long.TYPE, Long.TYPE);
		
		int index = ReflectionUtils.indexOfAnnotatedParameter(getMemberMessageMethod, PathParam.class);
		
		Assert.assertEquals("index", 0, index);
	}
	

}
