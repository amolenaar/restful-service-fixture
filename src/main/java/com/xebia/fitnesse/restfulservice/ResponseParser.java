package com.xebia.fitnesse.restfulservice;

public interface ResponseParser {

	public void parse(String content);
	
	public String getValue(String path);
}
