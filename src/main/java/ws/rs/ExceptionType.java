package ws.rs;

public interface ExceptionType
{
	public int getErrorCode();
	
	public String getErrorMessage();
	
	public int getStatusCode();
}
