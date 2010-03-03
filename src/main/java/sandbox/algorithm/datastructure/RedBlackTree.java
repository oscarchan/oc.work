package sandbox.algorithm.datastructure;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

/**
 * simple example to learn red-black tree in detail
 * @author ochan
 *
 * @param <K> key of the node
 * @param <V>
 */
public class RedBlackTree<K, V> 
{
	private Comparator<? super K> mComparator;

	private ColoredNode<K, V> mRoot;
	
	private int mSize;
	
	@SuppressWarnings("unchecked")
	public RedBlackTree()
	{
		mComparator = new NatureOrderComparator();
	}
	
	
	static class ColoredNode<K, V>
	{
		ColoredEntry<K, V> mEntry;
		
		ColoredNode<K, V> mLeft;
		ColoredNode<K, V> mRight;
	}
	static class ColoredEntry<K, V>
	{
		Color mColor;
		K mKey;
		V mValue;
	}
	
	static enum Color
	{
		red,
		black;
	}
	
	/** comparator using nature order */
	class NatureOrderComparator<T extends Comparable<T>>implements Comparator<T>
	{
		public int compare(T o1, T o2) 
		{
			return o1.compareTo(o2);
		}
		
	}

	public void clear() 
	{
		mRoot = null;
		mSize = 0;
	}

	public boolean containsKey(Object key) 
	{
		if(mRoot==null)
			return false;
		
		K  ky = (K) key;
		
		ColoredNode<K,V> node = getNode(ky);
		
		return node!=null;  // return true if the node exists 
	}

	public ColoredNode<K, V> getNode(K key)
	{
		ColoredNode<K, V> current = mRoot;
		
		while(current!=null) {
			int diff = mComparator.compare(current.mEntry.mKey, key);
		
			if(diff<0)
				current = current.mLeft;
			else if(diff>0)
				current = current.mRight;
			else
				return current;
		}

		return current;
	}

	/**
	 * left rotate the tree where X is the pivot point
	 * 
	 *    X             Y
	 *   / \           / \
	 *  a   y     =>  a   X
	 *     / \           / \ 
	 *    b   c         b   c
	 * 
	 * @param x
	 */
	private void leftRotate(ColoredNode<K, V> x)
	{
	}
	@SuppressWarnings("unchecked")
	public V get(Object key) 
	{
		K ky = (K) key;
		ColoredNode<K,V> node = getNode(ky);
		
		if(node!=null)
			return node.mEntry.mValue;
		
		return null;
	}

	public V put(K key, V value) {
		// TODO Auto-generated method stub
		return null;
	}

	

	public V remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	public int size() {
		return mSize;
	}

	
}

