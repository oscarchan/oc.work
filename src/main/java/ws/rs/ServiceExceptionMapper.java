package ws.rs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import ws.rs.ServiceException.ServiceExceptionType;

public class ServiceExceptionMapper implements ExceptionMapper<Throwable>
{
    private static final Log logger = LogFactory.getLog(ServiceExceptionMapper.class);

	public Response toResponse(Throwable t)
    {
        List<ErrorBody> errorList;
        int status; // avoid Response.Status class  since it does not contains all status codes

        if (t instanceof ServiceException) {
            ServiceException se = (ServiceException) t;
            status = se.getStatusCode();
            
        	if(status==0) {
        		logger.warn("unexpected empty status", t);
        		status = Status.INTERNAL_SERVER_ERROR.getStatusCode();
        	}
        	
        	errorList = Collections.singletonList(new ErrorBody(se.getExceptionType()));
        } else if (t instanceof WebApplicationException ) {
        	WebApplicationException e = (WebApplicationException) t;
        	status = e.getResponse().getStatus();
        	if(status==0) { 
        		logger.warn("unexpected empty status", e);
        		status = Status.INTERNAL_SERVER_ERROR.getStatusCode();
        	}
        	// if framework throws an error, it should be bad request form
        	errorList = Collections.singletonList(new ErrorBody(ServiceExceptionType.BAD_REQUEST));
        } else {
            logger.error("unknown error", t);
            status = Status.INTERNAL_SERVER_ERROR.getStatusCode();
            
        	errorList = Collections.singletonList(new ErrorBody(ServiceExceptionType.BAD_REQUEST));
        }

        return Response.status(status).type(MediaType.APPLICATION_JSON).entity(errorList).build();
    }
    
	static class ErrorBody
	{
		int errorCode;
		String errorMessage;
		
		// NULL == not to serialized
		List<ErrorField> errors = null;
		
		public ErrorBody(ExceptionType type)
		{
			errorCode = type.getErrorCode();
			errorMessage = type.getErrorMessage();
		}

		public int getErrorCode()
        {
        	return errorCode;
        }

		public String getErrorMessage()
        {
        	return errorMessage;
        }

		// skip if null
		@JsonSerialize(include=Inclusion.NON_NULL)
		public List<ErrorField> getErrors()
        {
        	return errors;
        }

		public void setErrorCode(int errorCode)
        {
        	this.errorCode = errorCode;
        }

		public void setErrorMessage(String errorMessage)
        {
        	this.errorMessage = errorMessage;
        }

		public void setErrors(List<ErrorField> errors)
        {
        	this.errors = errors;
        }

		
	}
	
	static class ErrorField
	{
		
	}
    

}
