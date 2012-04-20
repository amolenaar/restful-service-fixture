package com.xebia.fitnesse.restfulservice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Base Fixture class.
 */
public class RestfulServiceFixture {

	private HttpClient httpClient;
	private HttpEntity entity;
	private HttpResponse response;
	
	private ResponseParser responseParser;
	private String content;
	
	public RestfulServiceFixture() {
		httpClient = new DefaultHttpClient();
		responseParser = new JsonResponseParser();
	}
	
	public void httpGetRequest(String url) throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(url);
		executeRequest(httpGet);
	}

	public void setContent(String content) throws UnsupportedEncodingException {
		entity = new StringEntity(content, "text/json", "UTF-8");
	}
	
	public void setPostParameters(Map<String, String> parameters) throws UnsupportedEncodingException {
		System.out.println(parameters);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(parameters.size());
		for (Map.Entry<String, String> entry: parameters.entrySet()) {
			BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
			pairs.add(pair);
			System.out.println("post parameter pair: " + pair);
		}
		entity = new UrlEncodedFormEntity(pairs);
	}
	
	public void httpPostRequest(String url) throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);
		executeRequest(httpPost);
	}

	private void executeRequest(HttpUriRequest request) throws IOException,
			ClientProtocolException {
		response = httpClient.execute(request);
		content = EntityUtils.toString(response.getEntity());
		System.out.println("content: " + content);
		responseParser.parse(content);
	}

	public int statusCode() {
		assertResponse();
		return response.getStatusLine().getStatusCode();
	}

	public String contentType() {
		return header("Content-Type");
	}

	public String header(String header) {
		assertResponse();
		return response.getFirstHeader(header).getValue();
	}

	public String content() {
		assertResponse();
		return content;
	}
	
	public boolean contentContains(String substring) {
		assertResponse();
		return content.contains(substring);
	}

	public String path(String path) {
		assertResponse();
		return responseParser.getValue(path);
	}
	
	private void assertResponse() {
		if (response == null) {
			throw new AssertionError("First perform a Http GET or POST request");
		}
	}

	// For testing
	void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
}
