package sandbox.algorithm.datastructure;

import java.util.Comparator;

import sandbox.algorithm.datastructure.BinaryTreeMap.Entry;

/**
 * simple example to learn red-black tree in detail
 * @author ochan
 *
 * @param <K> key of the node
 * @param <V>
 */
public class RedBlackTree<K, V> 
{
	private static final Color RED = Color.red;
	private static final Color BLACK = Color.black;
	private Comparator<? super K> mComparator;

	private Entry<K, V> mRoot;
	
	private int mSize;
	
	@SuppressWarnings("unchecked")
	public RedBlackTree()
	{
		mComparator = new NatureOrderComparator();
	}
	
	
	static class Entry<K, V>
	{
		K key;
		V value;
		Color color = Color.black;
		
		Entry<K, V> parent;
		Entry<K, V> left;
		Entry<K, V> right;
		
		Entry(K key, V value, Entry<K, V> parent)
		{
			this.key = key;
			this.value = value;
			this.parent = parent;
		}
		
		V setValue(V newValue)
		{
			V old = value;
			value = newValue;
			return old;
		}
	}
	
	static enum Color
	{
		red,
		black;
	}
	
	public void clear() 
	{
		mRoot = null;
		mSize = 0;
	}

	@SuppressWarnings("unchecked")
	public boolean containsKey(Object key) 
	{
		if(key==null)
			throw new IllegalArgumentException("missing key");
		
		K  ky = (K) key;		
		Entry<K,V> node = getEntry(ky);
		
		return node!=null;  // return true if the node exists 
	}

	public V put(K key, V value)
	{		
		if (mRoot == null) {
			Entry<K, V> current = new Entry<K, V>(key, value, null);
			
			mRoot = current;
			mSize = 1;
			
			return null;
		}

		Entry<K, V> entryOrParent = getEntryOrParent(key);
		int diff = mComparator.compare(entryOrParent.key, key);
		
		if (diff==0) // if its the entry itself
			return entryOrParent.setValue(value);
		
		Entry<K, V> parent = entryOrParent;
		Entry<K, V> entry = new Entry<K, V>(key, value, parent);
		
		if(diff>0)
			parent.right = entry;
		else
			parent.left = entry;
	
		mSize++;
	
		fixAfterInsertion(entry);
		
		return null;
	}
	
    /**
     * Balancing operations.
     *
     * Implementations of rebalancings during insertion and deletion are
     * slightly different than the CLR version.  Rather than using dummy
     * nilnodes, we use a set of accessors that deal properly with null.  They
     * are used to avoid messiness surrounding nullness checks in the main
     * algorithms.
     */

    private static <K,V> Color colorOf(Entry<K,V> p) {
        return (p == null ? BLACK : p.color);
    }

    private static <K,V> Entry<K,V> parentOf(Entry<K,V> p) {
        return (p == null ? null: p.parent);
    }

    private static <K,V> void setColor(Entry<K,V> p, Color c) {
        if (p != null)
	    p.color = c;
    }

    private static <K,V> Entry<K,V> leftOf(Entry<K,V> p) {
        return (p == null) ? null: p.left;
    }

    private static <K,V> Entry<K,V> rightOf(Entry<K,V> p) {
        return (p == null) ? null: p.right;
    }

    private void rotateLeft(Entry<K,V> p) {
        if (p != null) {
            Entry<K,V> r = p.right;
            p.right = r.left;
            if (r.left != null)
                r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null)
                mRoot = r;
            else if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    /** From CLR */
    private void rotateRight(Entry<K,V> p) {
        if (p != null) {
            Entry<K,V> l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                mRoot = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }

	/** From CLR */
    private void fixAfterInsertion(Entry<K,V> x) 
    {
        x.color = RED;

        while (x != null && x != mRoot && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Entry<K,V> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Entry<K,V> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        mRoot.color = BLACK;
    }	
	
//	private Entry<K, V> predecessor(Entry<K, V> t)
//	{
//		if(t==null)
//			return null;
//		
//		if(t.left)
//	}
	
	private Entry<K, V> getEntry(K key)
	{
		Entry<K,V> entryOrParent = getEntryOrParent(key);
		
		if(entryOrParent==null)
			return null;
		
		if(mComparator.compare(key, entryOrParent.key)==0) // if not parent
			return entryOrParent;
		
		return null;
	}
	
	private Entry<K, V> getEntryOrParent(K key)
	{
		if(mRoot==null)
			return null;
		
		Entry<K, V> parent;
		Entry<K, V> current = mRoot;
		
		do {
			parent = current;
			int diff = mComparator.compare(current.key, key);
		
			if(diff<0)
				current = current.left;
			else if(diff>0)
				current = current.right;
			else
				return current;
		} while (current!=null);

		return parent;
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
	private void leftRotate(Entry<K, V> x)
	{
	}
	@SuppressWarnings("unchecked")
	public V get(Object key) 
	{
		K ky = (K) key;
		Entry<K,V> node = getEntry(ky);
		
		if(node!=null)
			return node.value;
		
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

