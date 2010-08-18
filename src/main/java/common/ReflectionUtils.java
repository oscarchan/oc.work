package common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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
		
		if(fromIndex>=paramAnnotations.length)
			throw new IllegalArgumentException("invalid fromIndex: " + fromIndex);
		
		for(int argIndex=fromIndex;argIndex<paramAnnotations.length;fromIndex++) {
			Annotation[] paramMarkers= paramAnnotations[argIndex];
			
			for (Annotation paramMarker : paramMarkers) {
				if(markerClass.isAssignableFrom(paramMarker.annotationType()))
					return argIndex;
            }
		}
		
		return -1;
	}
}
