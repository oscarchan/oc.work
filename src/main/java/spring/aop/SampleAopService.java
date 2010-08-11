package spring.aop;

import org.springframework.transaction.annotation.Transactional;

public class SampleAopService
{
	@Transactional
	public Object getMethod_tx()
	{
		return "getMethod_tx";
	}
	
	@Transactional(readOnly=true)
	public Object getMethod_readOnlyTx()
	{
		return "getMethod_readOnlyTx";
	}
}
