package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchTree<T> {
	private int size;
	private Node root;

	private class Node {
		private char c;
		private Node left, mid, right;
		private transient Object[] values;
		
		public boolean add(T val) {
			if (values == null)
				values = new Object[] {val};
			else {
				if (Arrays.asList(values).contains(val))
					return false;
				values = Arrays.copyOf(values, values.length + 1);
				values[values.length - 1] = val;
			}
			return true;
		}
	}

	// return number of key-value pairs
	public int size() {
		return size;
	}

	/**************************************************************
	 * Is string key in the symbol table?
	 **************************************************************/
	public boolean containsKey(String key) {
		return get(key) != null;
	}

	@SuppressWarnings("unchecked")
	public T[] get(String key) {
		if (key == null || key.length() == 0)
			throw new RuntimeException("illegal key");
		Node x = get(root, key, 0);
		if (x == null)
			return null;
		return (T[]) x.values;
	}

	// return subtrie corresponding to given key
	private Node get(Node x, String key, int d) {
		if (key == null || key.length() == 0)
			throw new RuntimeException("illegal key");
		if (x == null)
			return null;
		char c = Character.toLowerCase(key.charAt(d));
		if (c < x.c)
			return get(x.left, key, d);
		else if (c > x.c)
			return get(x.right, key, d);
		else if (d < key.length() - 1)
			return get(x.mid, key, d + 1);
		else
			return x;
	}

	/**************************************************************
	 * Insert string s into the symbol table.
	 **************************************************************/
	public void put(String s, T val) {
//		if (!containsKey(s))
//			size++;
		
		root = put(root, s, val, 0);
	}

	private Node put(Node x, String s, T val, int d) {
		char c = Character.toLowerCase(s.charAt(d));
		if (x == null) {
			x = new Node();
			x.c = c;
		}
		if (c < x.c)
			x.left = put(x.left, s, val, d);
		else if (c > x.c)
			x.right = put(x.right, s, val, d);
		else if (d < s.length() - 1)
			x.mid = put(x.mid, s, val, d + 1);
		else
			size = x.add(val) ? size + 1 : size;
		return x;
	}

	/**************************************************************
	 * Find and return longest prefix of s in TST
	 **************************************************************/
	public String longestPrefixOf(String s) {
		if (s == null || s.length() == 0)
			return null;
		int length = 0;
		Node x = root;
		int i = 0;
		while (x != null && i < s.length()) {
			char c = s.charAt(i);
			if (c < x.c)
				x = x.left;
			else if (c > x.c)
				x = x.right;
			else {
				i++;
				if (x.values != null)
					length = i;
				x = x.mid;
			}
		}
		return s.substring(0, length);
	}

	// all keys in symbol table
	public Iterable<String> keys() {
		List<String> queue = new ArrayList<String>();
		collectKeys(root, "", queue);
		return queue;
	}

	// all keys starting with given prefix
	public Iterable<String> prefixMatchingKeys(String prefix) {
		Node x = get(root, prefix, 0);
		if (x == null)
			return Collections.emptyList();
		
		List<String> queue = new ArrayList<String>();
		if (x.values != null)
			queue.add(prefix);
		collectKeys(x.mid, prefix, queue);
		return queue;
	}
	// all keys in subtrie rooted at x with given prefix
	private void collectKeys(Node x, String prefix, List<String> queue) {
		if (x == null)
			return;
		collectKeys(x.left, prefix, queue);
		if (x.values != null)
			queue.add(prefix + x.c);
		collectKeys(x.mid, prefix + x.c, queue);
		collectKeys(x.right, prefix, queue);
	}
	

	// return all keys matching given wilcard pattern
	public Iterable<String> wildcardMatchingKeys(String pat) {
		List<String> queue = new ArrayList<String>();
		collect(root, "", 0, pat, queue);
		return queue;
	}

	private void collect(Node x, String prefix, int i, String pat, List<String> q) {
		if (x == null)
			return;
		char c = pat.charAt(i);
		if (c == '.' || c < x.c)
			collect(x.left, prefix, i, pat, q);
		if (c == '.' || c == x.c) {
			if (i == pat.length() - 1 && x.values != null)
				q.add(prefix + x.c);
			if (i < pat.length() - 1)
				collect(x.mid, prefix + x.c, i + 1, pat, q);
		}
		if (c == '.' || c > x.c)
			collect(x.right, prefix, i, pat, q);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> prefixMatchingValues(String prefix) {
		Node x = get(root, prefix, 0);
		if (x == null)
			return Collections.emptyList();
		
		ArrayList<T> queue = new ArrayList<T>();
		if (x.values != null)
			queue.addAll(Arrays.asList((T[])x.values));
		collectValues(x.mid, prefix, queue);
		return queue;
	}
	
	
	@SuppressWarnings("unchecked")
	private void collectValues(Node x, String prefix, ArrayList<T> queue) {
		if (x == null)
			return;
		collectValues(x.left, prefix, queue);
		if (x.values != null)
			queue.addAll(Arrays.asList((T[])x.values));
		collectValues(x.mid, prefix + x.c, queue);
		collectValues(x.right, prefix, queue);
	}

	// test client
	public static void main(String[] args) {
		
		// build symbol table from standard input
		SearchTree<Integer> st = new SearchTree<Integer>();
		
		st.put("nici", 1);
		st.put("Nici", 2);
		st.put("Schmalz", 3);
		st.put("\u03B1\u03B2\u03B3 - abc", 5);
		// print results
		for (String key : st.keys()) {
			System.out.println(key + " " + Arrays.toString(st.get(key)));
		}
		
		System.out.println(st.prefixMatchingValues("nic"));
	}
}
