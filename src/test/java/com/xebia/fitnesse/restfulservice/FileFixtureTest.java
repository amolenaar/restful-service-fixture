package com.xebia.fitnesse.restfulservice;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class FileFixtureTest {

	@Test
	public void testReadingAFile() throws Exception {
		File file = new File("pom.xml");
		FileFixture fixture = new FileFixture();
		fixture.expectOutput("xml");
		fixture.readFile(file);
		assertEquals(file.length(), fixture.content().length());
		assertEquals("com.xebia.fitnesse", fixture.path("/project/groupId/text()"));
	}


	@Test
	public void testReadingAFileByName() throws Exception {
		String filename = "pom.xml";
		FileFixture fixture = new FileFixture();
		fixture.expectOutput("xml");
		fixture.readFile(filename);
		assertEquals(new File(filename).length(), fixture.content().length());
		assertEquals("com.xebia.fitnesse", fixture.path("/project/groupId/text()"));
	}
	
	@Test
	public void testExtractingAValue() throws Exception {
		String filename = "pom.xml";
		FileFixture fixture = new FileFixture();
		fixture.expectOutput("xml");
		fixture.readFile(filename);
		assertEquals("com.xebia.fitnesse", fixture.path("/project/groupId/text()"));
	}
	
	
}
