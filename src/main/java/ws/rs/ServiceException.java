package ws.rs;


public class ServiceException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    private ExceptionType exceptionType;
    public ServiceException(ExceptionType type) {
    	super(exceptionTypeToString(type));
    	exceptionType = type;
    }
    
    public ServiceException(ExceptionType type, Throwable t) {
    	super(exceptionTypeToString(type), t);
    	exceptionType = type;
    }
    
    public ServiceException(ExceptionType type, String message) {
    	super(message + exceptionTypeToString(type));
    	exceptionType = type;
    }
    
    public ServiceException(ExceptionType type, String message, Throwable t) {
    	super(message + exceptionTypeToString(type), t);
    	exceptionType = type;
    }
    
    private static String exceptionTypeToString(ExceptionType type) {
    	return " [ErrorCode: " + type.getErrorCode() + ", ErrorMessage: " + type.getErrorMessage() + "]";
    }
    
    public int getErrorCode() {
		return exceptionType.getErrorCode();
	}

	public String getErrorMessage() {
		return exceptionType.getErrorMessage();
	}

	public int getStatusCode() {
		return exceptionType.getStatusCode();
	}
	
	public ExceptionType getExceptionType() {
		return exceptionType;
	}
    
    public static enum ServiceExceptionType implements ExceptionType
    {
		OK(200, 10200, "OK"),
		CREATED(201, 10201, "created"),
		BAD_REQUEST(400, 10400, "bad request"),
		UNPROCESSABLE_ENTITY(422, 10422, "unprocessable entity"),
		INTERNAL_SERVER_ERROR(500, 10500, "internal server error"),
		NOT_IMPLEMENTED(501, 10501, "not implemented");
		
		private int statusCode;
		private int errorCode;
		private String errorMessage;
		
		private ServiceExceptionType(int statusCode, int errorCode, String errorMessage) {
			this.statusCode = statusCode;
			this.errorCode = errorCode;
			this.errorMessage = errorMessage;
		}

		public int getStatusCode()
        {
        	return statusCode;
        }

		public int getErrorCode()
        {
        	return errorCode;
        }

		public String getErrorMessage()
        {
        	return errorMessage;
        }
		
		public static ServiceExceptionType getByErrorCode(int code)
		{
			ServiceExceptionType[] values = ServiceExceptionType.values();
			for (ServiceExceptionType type : values) {
	            if(type.errorCode==code)
	            	return type;
            }
			
			return null;
		}
		
    }
    
    
}
