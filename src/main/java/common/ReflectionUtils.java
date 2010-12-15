package common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class ReflectionUtils
{
	/**
	 * return the first index within the method parameters that match the annotation
	 * @param method
	 * @param annotation
	 * @return
	 */
	public static int indexOfAnnotatedParameter(Method method, Class<? extends Annotation> annotation)
	{
		return indexOfAnnotatedParameter(method, annotation, 0);
	}
	
	/**
	 * return the first index within the method parameters (starting with fromIndex position) that match the annotation 
	 * @param method
	 * @param markerClass
	 * @param fromIndex
	 * @return
	 */
	public static int indexOfAnnotatedParameter(Method method, Class<? extends Annotation> markerClass, int fromIndex)
	{
		if(method==null)
			throw new IllegalArgumentException("missing method");
		
		if(markerClass==null)
			throw new IllegalArgumentException("missing annotation");
		
		if(fromIndex<0)
			throw new IllegalArgumentException("invalid fromIndex: " + fromIndex);
		
		Annotation[][] paramAnnotations = method.getParameterAnnotations();
		
		if(fromIndex>paramAnnotations.length)
			throw new IllegalArgumentException("invalid fromIndex: " + fromIndex);
		
		for(int argIndex=fromIndex;argIndex<paramAnnotations.length;argIndex++) {
			Annotation[] paramMarkers= paramAnnotations[argIndex];
			
			for (Annotation paramMarker : paramMarkers) {
				if(markerClass.isAssignableFrom(paramMarker.annotationType()))
					return argIndex;
            }
		}
		
		return -1;
	}
	
	@SuppressWarnings("unchecked")
    public static <A extends Annotation> A getParameterAnnotation(Method method, Class<A> markerClass, int index)
	{
		Annotation[][] paramAnnotations = method.getParameterAnnotations();

		if(index<0 || index >=paramAnnotations.length)
			throw new IllegalArgumentException("wrong index: " + index + ": method=" + method.toGenericString());
		
		Annotation[] annotations = paramAnnotations[index];
		
		for (Annotation annotation : annotations) {
				if(markerClass.isAssignableFrom(annotation.annotationType()))
					return (A) annotation;
        }
		
		return null;
		
	}
	
		public static StringBuilder getMethodSignature(JoinPoint jp, boolean parameterLoggable)
	{
		MethodSignature signature = (MethodSignature) jp.getSignature();
		Method method = signature.getMethod();
		Object[] args = jp.getArgs();

		return getMethodSignature(method, args, parameterLoggable);
	}
	
	/**
	 * return method signature with parameters
	 * @param method
	 * @param args
	 * @param parameterLoggable
	 * @return
	 */
	public static StringBuilder getMethodSignature(Method method, Object[] args, boolean parameterLoggable)
	{
		StringBuilder sb = new StringBuilder();
		
	    sb.append(method.getName());
		
		sb.append("(");
		
		if(parameterLoggable) {
			for (int i = 0; args != null && i < args.length; i++) {
				if (i != 0)
					sb.append(", ");
	
				sb.append(args[i]);
			}
		} else if (args==null || args.length == 0) {
			// nothing
		} else { 
			// omit the parameters
			sb.append("..");
		}
		
		sb.append(")");

		return sb;		
	}
}
