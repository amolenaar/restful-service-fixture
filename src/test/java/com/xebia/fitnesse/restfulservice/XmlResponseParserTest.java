package com.xebia.fitnesse.restfulservice;

import static org.junit.Assert.*;

import org.junit.Test;

public class XmlResponseParserTest {

	@Test
	public void testSingleItem() {
		String xml = "<foo>bar</foo>";
		XmlResponseParser parser = new XmlResponseParser();
		parser.parse(xml);
		assertEquals("bar", parser.getValue("/foo/text()"));
	}
	
	@Test
	public void testNoDataFound() {
		String xml = "<foo>bar</foo>";
		XmlResponseParser parser = new XmlResponseParser();
		parser.parse(xml);
		assertNull(parser.getValue("/foo/bar/text()"));
	}

	@Test
	public void testMultipleItems() {
		String xml = "<foo><bar>one</bar><bar>two</bar></foo>";
		XmlResponseParser parser = new XmlResponseParser();
		parser.parse(xml);
		assertEquals("[\"one\",\"two\"]", parser.getValue("/foo/bar/text()"));
	}
}
