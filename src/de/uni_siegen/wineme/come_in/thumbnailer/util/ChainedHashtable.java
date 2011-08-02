/*
 * regain/Thumbnailer - A file search engine providing plenty of formats (Plugin)
 * Copyright (C) 2011  Come_IN Computerclubs (University of Siegen)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: Come_IN-Team <come_in-team@listserv.uni-siegen.de>
 */

package de.uni_siegen.wineme.come_in.thumbnailer.util;

import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Hashtable that can contain several entries per key.
 * (Helper Class)
 * 
 * @param <K>	Key
 * @param <V>	Value
 */
public class ChainedHashtable<K, V> implements Map<K, V> {

	private static final int DEFAULT_HASHTABLE_SIZE = 20;

	private static final int DEFAULT_LIST_SIZE = 10;
	private int listSize;
	
	Hashtable<K, Deque<V>> hashtable;
	int size;
	
	public ChainedHashtable()
	{
		this(DEFAULT_HASHTABLE_SIZE);
	}
	
	public ChainedHashtable(int hashtableSize) {
		this(hashtableSize, DEFAULT_LIST_SIZE);
	}
	public ChainedHashtable(int hashtableSize, int chainSize) {
		hashtable = new Hashtable<K, Deque<V>>(hashtableSize);
		listSize = chainSize;
		
		size = 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return hashtable.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		if (isEmpty())
			return false;

		Enumeration<Deque<V>> elements = hashtable.elements();
		
		Deque<V> list = null;
		while (elements.hasMoreElements())
		{	
			list = elements.nextElement();
			if (list.contains(value))
				return true;
		}
		return false;
	}

	@Override
	/**
	 * Get first of the linked objects by this key.
	 */
	public V get(Object key) {
		Deque<V> list = hashtable.get(key);
		if (list == null)
			return null;
		else
			return list.getFirst();
	}
	
	/**
	 * Get all objects linked by this key
	 * as an Iterable usable an foreach loop.
	 * 
	 * @param key
	 * @return	Iterable
	 * @throws NullPointerException (if key null)
	 */
	public Iterable<V> getIterable(Object key) {
		final Queue<V> list = hashtable.get(key);
		
		if (list == null)
		{
			// Empty Iterator
			return new Iterable<V>() {
				public Iterator<V> iterator() {
					return new Iterator<V>() {
						public boolean hasNext() { return false; }
						public V next() { return null; }
						public void remove() { }
					};
				}
			};
		}
		else
		{
			// Iterator of list
			return new Iterable<V>() {
				public Iterator<V> iterator() {
					return list.iterator();
				}
			};
		}
	}

	@Override
	/**
	 * Add this Value at the end of this key.
	 * @return As the value is never replaced, this will always return null.
	 */
	public V put(K key, V value) {
		boolean success;
		
		Deque<V> list = hashtable.get(key);
		if (list == null)
		{
			list = new ArrayDeque<V>(listSize);
			success = list.offerLast(value);
			hashtable.put(key, list);
		}
		else
			success = list.offerLast(value);
		
		if (success)
			size++;
		
		return null;
	}

	@Override
	/**
	 * Remove all objects linked to this key.
	 * 
	 * @param key	Key
	 * @return First of linked objects (or null).
	 */
	public V remove(Object key) {
		Deque<V> list = hashtable.remove(key);
		if (list == null)
			return null;
		else
		{
			V element = list.getFirst();
			size -= list.size();
			return element;
		}
	}
	
	public boolean remove(K key, V value)
	{
		Deque<V> list = hashtable.get(key);
		
		if (list == null)
			return false;
		
		boolean removed = list.remove(value);
		if (removed)
		{
			if (list.isEmpty())
				hashtable.remove(key);
			size--;
		}
		return removed;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		hashtable.clear();
		size = 0;
	}

	@Override
	public Set<K> keySet() {
		return hashtable.keySet();
	}

	@Override
	public Collection<V> values() {
		List<V> newList = new ArrayList<V>();
		
		if (isEmpty())
			return newList;
		
		Enumeration<Deque<V>> elements = hashtable.elements();
		
		Deque<V> list = null;
		while (elements.hasMoreElements())
		{	
			list = elements.nextElement();
			newList.addAll(list);
		}

		return newList;
	}

	@Override
	// Not sure whether this will when 2 identical (equal) entries are added: same key & same value
	public Set<Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> entries = new HashSet<Map.Entry<K, V>>();
		for (K key : hashtable.keySet())
		{
			Deque<V> values = hashtable.get(key);
			for (V value : values)
			entries.add(new AbstractMap.SimpleEntry<K,V>(key, value));
		}
		return entries;
	}	
	
	public String toString()
	{
		StringBuffer str = new StringBuffer(200);
		
		for (K key : hashtable.keySet())
		{
			str.append(key).append(":\n");
			
			Deque<V> values = hashtable.get(key);
			for (V value : values)
				str.append("\t").append(value).append("\n");
			str.append("\n");
		}
		
		return str.toString();
	}
}
