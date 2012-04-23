package com.xebia.fitnesse.restfulservice;

import java.io.File;

public interface ResponseParser {

	void parse(String content) throws Exception;

	void parse(File file) throws Exception;
	
	String getValue(String path);
}
