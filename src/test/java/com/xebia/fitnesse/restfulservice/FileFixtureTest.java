package com.xebia.fitnesse.restfulservice;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class FileFixtureTest {

	@Test
	public void testReadingAFile() throws IOException {
		File file = new File("pom.xml");
		FileFixture fixture = new FileFixture();
		fixture.readFile(file);
		assertEquals(file.length(), fixture.content().length());
	}


	@Test
	public void testReadingAFileByName() throws IOException {
		String filename = "pom.xml";
		FileFixture fixture = new FileFixture();
		fixture.readFile(filename);
		assertEquals(new File(filename).length(), fixture.content().length());
	}
	
	@Test
	public void testExtractingAValue() throws IOException {
		String filename = "pom.xml";
		FileFixture fixture = new FileFixture();
		fixture.expectOutput("xml");
		fixture.readFile(filename);
		assertEquals("com.xebia.fitnesse", fixture.path("/project/groupId/text()"));
	}
	
	
}
