package common.time;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.apache.commons.lang.mutable.MutableLong;

/**
 * tracking the time of period with  
 * @author ochan
 *
 * @param <K>
 * @param <T>
 */
public class TimeSlice<K extends Comparable<K>, T>
{
	private LinkedHashMap<K, MutableLong> buckets;

	// factory
	private ComparableKeyFactory<K> keyFactory;
	private BucketFactory<T> bucketFactory;
	
	// keep the last X buckets
	private LinkedHashMap<K, T> lastBuckets;
	
	/** max number of buckets to be kept */
	private int maxBuckets;
	
	public T getBucket()
	{
		return null;
	}
	
	public Iterable<T> getBuckets()
	{
		return null;
	}
	
	public void addBucketExpireListener()
	{
		
	}
	
	public void addBucketCreateListener()
	{
		
	}

	public interface ComparableKeyFactory<K>
	{
		K getCurrentKey();
		/** 
		 * index 
		 */
		K getKey(int index);
	}
	
	public static interface BucketFactory<T>
	{
		
		public T getBucket();
	}
}
