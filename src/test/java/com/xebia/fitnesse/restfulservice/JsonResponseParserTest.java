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
	public void testInvalidPath() {
		String json = "{ \"foo\": { \"bar\": \"baz\" } }";
		
		JsonResponseParser parser = new JsonResponseParser();
		parser.parse(json);
		assertNull(parser.getValue("foo.nonexistant"));
	}

	@Test
	public void testList() {
		String json = "[ \"foo\", \"bar\", \"baz\" ]";
		
		JsonResponseParser parser = new JsonResponseParser();
		parser.parse(json);
		assertEquals("foo", parser.getValue("$[0]"));
		assertEquals("bar", parser.getValue("$[1]"));
		assertEquals("baz", parser.getValue("$[2]"));
		assertNull(parser.getValue("$[3]"));
	}

	@Test
	public void testObjectWithList() {
		String json = "{ \"foo\": [ \"bar\", \"baz\" ] }";
		
		JsonResponseParser parser = new JsonResponseParser();
		parser.parse(json);
		assertEquals("bar", parser.getValue("foo[0]"));
		assertEquals("baz", parser.getValue("foo[1]"));
		assertNull(parser.getValue("foo[2]"));
	}

	@Test
	public void testMultipleItemsFromList() {
		String json = "{ \"foo\": [ \"bar\", \"baz\" ] }";
		
		JsonResponseParser parser = new JsonResponseParser();
		parser.parse(json);
		assertEquals("[\"bar\",\"baz\"]", parser.getValue("foo"));
	}

}
