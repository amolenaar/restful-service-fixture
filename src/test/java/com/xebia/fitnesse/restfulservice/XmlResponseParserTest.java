package com.xebia.fitnesse.restfulservice;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class XmlResponseParserTest {

	@Test
	public void testSingleItem() throws Exception {
		String xml = "<foo>bar</foo>";
		XmlResponseParser parser = new XmlResponseParser();
		parser.parse(xml);
		assertEquals("bar", parser.getValue("/foo/text()"));
	}
	
	@Test
	public void testNoDataFound() throws Exception {
		String xml = "<foo>bar</foo>";
		XmlResponseParser parser = new XmlResponseParser();
		parser.parse(xml);
		assertNull(parser.getValue("/foo/bar/text()"));
	}

	@Test
	public void testMultipleItems() throws Exception {
		String xml = "<foo><bar>one</bar><bar>two</bar></foo>";
		XmlResponseParser parser = new XmlResponseParser();
		parser.parse(xml);
		assertEquals("[\"one\",\"two\"]", parser.getValue("/foo/bar/text()"));
	}

	@Test
	public void testFile() throws Exception {
		File xml = new File("src/test/resources/testdata.xml");
		XmlResponseParser parser = new XmlResponseParser();
		parser.parse(xml);
		assertEquals("baz", parser.getValue("/foo/bar/text()").trim());
	}

	@Test
	public void testNamespacedFile() throws Exception {
		File xml = new File("pom.xml");
		XmlResponseParser parser = new XmlResponseParser();
		parser.parse(xml);
		assertEquals("com.xebia.fitnesse", parser.getValue("/project/groupId/text()").trim());
	}

}
