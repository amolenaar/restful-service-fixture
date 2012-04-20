package com.xebia.fitnesse.restfulservice;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;

public class JsonResponseParser implements ResponseParser {

	private String content;
	
	public void parse(String content) {
		this.content = content;
	}

	public String getValue(String path) {
		try {
		return JsonPath.read(content, path).toString();
		} catch (InvalidPathException e) {
			System.err.println(e.getMessage());
			return null;
		} catch (IndexOutOfBoundsException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

}
