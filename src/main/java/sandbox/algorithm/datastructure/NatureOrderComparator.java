package sandbox.algorithm.datastructure;

import java.util.Comparator;

/**
 * comparator using nature order 
 */
class NatureOrderComparator<T extends Comparable<T>>implements Comparator<T>
{
	public int compare(T o1, T o2) 
	{
		return o1.compareTo(o2);
	}
	
}