package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.util.Collection;
import java.util.Map;

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
	
	public void testRemoveSimple()
	{
		data.put(2, "two");
		data.put(1, "one");
		
		assertNotNull(data.remove(1));
		assertTrue(data.remove(2, "two"));
		
		assertSize(0, 0, data);
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
	
	public void testEntrySetUnsupport()
	{
		try {
			data.entrySet();
			fail("entrySet should raise an unsupported exception");
		} catch (UnsupportedOperationException ex) {
			// OK
		}
	}
	
	public void testIdenticalEntries()
	{
		data.put(1, "one");
		data.put(1, "one");
		data.put(2, "one");
		data.put(1, "two");
		assertSize(4, 2, data);
		
		int identEntries = 0;
		for (Map.Entry<Integer, String> entry : data)
		{
			if (entry.getKey().equals(1) && "one".equals(entry.getValue()))
				identEntries++;
		}
		assertEquals("Pair 1-one was not found 2 times", 2, identEntries);
	}
	
	public void testCopyConstruct()
	{
		data.put(1, "one");
		data.put(2, "two");
		data.put(2, "zwei");
		assertSize(3,2, new ChainedHashtable<Integer, String>(data));
	}
	
	public void testBackingValues()
	{
		// "The set is backed by the map, so changes to the map are reflected in the set, and vice-versa."
		data.put(1, "one");
		data.put(2, "two");
		assertSize(2, 2, data);
		
		Collection<String> collection = data.values();
		assertTrue(collection.remove("one"));
		assertSize(1, 1, data);
		
		assertTrue(data.remove(2, "two"));
		assertSize(0, 0, data);
		assertEquals("Hashmap changed, but collection(values()) didn't", 0, collection.size());
	}
	
	public void testBackingKeys()
	{
		// "The set is backed by the map, so changes to the map are reflected in the set, and vice-versa."
		data.put(1, "one");
		data.put(2, "two");
		assertSize(2, 2, data);
		
		Collection<String> collection = data.values();
		assertTrue(collection.remove("one"));
		assertSize(1, 1, data);

		assertTrue(data.remove(2, "two"));
		assertSize(0, 0, data);
		assertEquals("Hashmap changed, but collection(values()) didn't", 0, collection.size());
	}

	@SuppressWarnings("unused") 
	public void testBackingIterableKey()
	{
		data.put(1, "one");
		data.put(2, "two");
		assertSize(2, 2, data);

		Iterable<String> iterable = data.getIterable(2);
		data.put(2, "zwei");
		int i = 0;
		for (String test : iterable)
			i++;
		assertEquals("Hashmap changed, but getIterable didn't", 2, i);
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
		assertEquals("Size of Values is not correct.\n" + hashy + "\n" + hashy.values(), size, hashy.values().size());
		
		assertEquals("Size of Keys is not correct.\n" + hashy, keySize, hashy.keySet().size());
	}
}
