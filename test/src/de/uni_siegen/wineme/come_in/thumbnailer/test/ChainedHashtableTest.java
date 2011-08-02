package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.util.Collection;

import de.uni_siegen.wineme.come_in.thumbnailer.util.ChainedHashtable;
import junit.framework.TestCase;

public class ChainedHashtableTest extends TestCase {

	private ChainedHashtable<Integer, String> data;

	public void setUp()
	{
		data = new ChainedHashtable<Integer, String>();
	}
	
	public void testContains()
	{
		data.put(1, "one");
		data.put(2, "two");
		data.put(2, "zwei");
		
		assertTrue("containsValue one didn't work", data.containsValue("one"));
		assertTrue(data.containsValue("two"));
		assertTrue(data.containsValue("zwei"));
		assertTrue(data.containsKey(1));
		assertTrue(data.containsKey(2));		
	}
	
	public void testRemove()
	{
		data.put(1, "one");
		data.put(2, "two");
		data.put(2, "zwei");
		assertSize(3, 2, data);
		
		data.remove(1);
		assertSize(2, 1, data);
		for (String test: data.getIterable(1))
			fail("Key 1 was not empty: " + test);
		for (Integer key : data.keySet())
		{
			if (key.equals(1))
				fail("keySet contained key 1, which was removed.");
		}
		assertFalse("containsKey contained key 1, which was removed.", data.containsKey(1));
		assertFalse("!containsValue one", data.containsValue("one"));
		
		data.clear();
		assertSize(0, 0, data);
		assertTrue("isEmpty", data.isEmpty());
	}
	
	public void testRemoveValue()
	{
		data.put(1, "one");
		data.put(2, "two");
		data.put(2, "zwei");
		assertSize(3, 2, data);
		
		data.remove(1, "one");
		assertSize(2, 1, data);

		data.remove(2, "two");
		assertSize(1, 1, data);
	}
	
	
	public void testIdenticalEntries()
	{
		data.put(1, "one");
		assertEquals("one", data.put(1, "one"));
		data.put(2, "one");
		data.put(1, "two");
		assertSize(3, 2, data);		
	}
	
	public void testBacking()
	{
		// "The set is backed by the map, so changes to the map are reflected in the set, and vice-versa."
		data.put(1, "one");
		data.put(2, "two");
		assertSize(2, 2, data);
		
		Collection<String> collection = data.values();
		collection.remove("one");
		assertSize(1, 1, data);
		data.remove(2, "two");
		assertEquals("Hashmap changed, but collection didn't", 0, collection.size());
	}
	
	public void testOrder()
	{
		data.put(2, "two");
		data.put(1, "one");
		data.put(2, "zwei");
		assertEquals("two", data.get(2));
	}
	
	private void assertSize(int size, int keySize, ChainedHashtable<Integer, String> hashy)
	{
		assertEquals("Size is not correct.\n" + hashy, size, hashy.size());
		assertEquals("Size of Entries is not correct.\n" + hashy, size, hashy.entrySet().size());
		assertEquals("Size of Values is not correct.\n" + hashy + "\n" + hashy.values(), size, hashy.values().size());
		
		assertEquals("Size of Keys is not correct.\n" + hashy, keySize, hashy.keySet().size());
	}
}
