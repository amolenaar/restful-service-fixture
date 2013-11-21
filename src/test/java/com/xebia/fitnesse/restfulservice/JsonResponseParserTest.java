package com.xebia.fitnesse.restfulservice;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonResponseParserTest {

	@Test
	public void testObject() {
		String json = "{ \"foo\": \"bar\" }";
		
		JsonResponseParser parser = new JsonResponseParser();
		parser.parse(json);
		assertEquals("bar", parser.getValue("foo"));
	}

	@Test
	public void testNestedObjects() {
		String json = "{ \"foo\": { \"bar\": \"baz\" } }";
		
		JsonResponseParser parser = new JsonResponseParser();
		parser.parse(json);
		assertEquals("baz", parser.getValue("foo.bar"));
		assertEquals("{\"bar\":\"baz\"}", parser.getValue("foo"));
	}

   @Test
    public void testCounting() {
        String json = "{ \"foo\": { \"bar\": 99, \"baz\": [1,2,3], \"boo\": { \"a\": 1, \"b\": 2 } } }";
        
        JsonResponseParser parser = new JsonResponseParser();
        parser.parse(json);
        assertEquals(1, parser.getCount("foo.bar"));
        assertEquals(3, parser.getCount("foo.baz"));
        assertEquals(2, parser.getCount("foo.boo"));
    }

	@Test
	public void testInvalidPath() {
		String json = "{ \"foo\": { \"bar\": \"baz\" } }";
		
		JsonResponseParser parser = new JsonResponseParser();
		parser.parse(json);
		assertEquals("Error: invalid path (foo.nonexistant)", parser.getValue("foo.nonexistant"));
	}

	@Test
	public void testList() {
		String json = "[ \"foo\", \"bar\", \"baz\" ]";
		
		JsonResponseParser parser = new JsonResponseParser();
		parser.parse(json);
		assertEquals("foo", parser.getValue("$[0]"));
		assertEquals("bar", parser.getValue("$[1]"));
		assertEquals("baz", parser.getValue("$[2]"));
		assertEquals("Error: index out of bounds ($[3])", parser.getValue("$[3]"));
	}

	@Test
	public void testObjectWithList() {
		String json = "{ \"foo\": [ \"bar\", \"baz\" ] }";
		
		JsonResponseParser parser = new JsonResponseParser();
		parser.parse(json);
		assertEquals("bar", parser.getValue("foo[0]"));
		assertEquals("baz", parser.getValue("foo[1]"));
		assertEquals("Error: index out of bounds (foo[2])", parser.getValue("foo[2]"));
	}

	@Test
	public void testObjectSelection() {
		String json = "{ \"foo\": [ { \"field\": \"bar\" }, { \"field\": \"baz beer\" } ] }";
		
		JsonResponseParser parser = new JsonResponseParser();
		parser.parse(json);
		assertEquals("baz beer", parser.getValue("foo[1].field"));
		assertEquals(1, parser.getCount("$.foo[?(@.field == bar)]"));
		assertEquals(1, parser.getCount("$.foo[?(@.field == baz beer)]"));
	}

	@Test
	public void testMultipleItemsFromList() {
		String json = "{ \"foo\": [ \"bar\", \"baz\" ] }";
		
		JsonResponseParser parser = new JsonResponseParser();
		parser.parse(json);
		assertEquals("[\"bar\",\"baz\"]", parser.getValue("foo"));
	}

}
