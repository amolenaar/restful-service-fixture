package com.xebia.fitnesse.restfulservice;

import static com.xebia.fitnesse.restfulservice.FitNesseUtil.asFile;

import java.io.File;
import java.io.FileReader;

public class FileFixture {

	private String content;
	private ResponseParser responseParser;
	
	public FileFixture() {
		expectOutput("json");
	}

	public void expectOutput(String format) {
		if ("json".equalsIgnoreCase(format)) {
			responseParser = new JsonResponseParser();
		} else if ("xml".equalsIgnoreCase(format)) {
			responseParser = new XmlResponseParser();
		}
	}

	public void readFile(String filename) throws Exception {
		File file = asFile(filename);
		
		readFile(file);
	}

	void readFile(File file) throws Exception {
		FileReader reader = new FileReader(file);
		char buf[] = new char[512];
		StringBuilder builder = new StringBuilder(512);
		
		int charsRead;
		do {
			charsRead = reader.read(buf);
			if (charsRead > 0) {
				builder.append(buf, 0, charsRead);
			}
		} while (charsRead > 0);
		content = builder.toString();
		responseParser.parse(content);
	}
	
	public String content() {
		return content;
	}
	
	public String path(String path) {
		assertContent();
		return responseParser.getValue(path);
	}

	private void assertContent() {
		if (content == null) {
			throw new AssertionError("First read a file");
		}
	}
	
	/**
	 * 'Namespaced' version of {@link #content()}.
	 * 
	 * @return
	 */
	public String fileContent() {
		return content();
	}
	
	/**
	 * 'Namespaced' version of {@link #path()}.
	 * 
	 * @return
	 */
	public String filePath(String path) {
		return path(path);
	}

}
