package com.xebia.fitnesse.restfulservice;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * Base Fixture class.
 */
public class WebServiceFixture {

	private HttpClient httpClient;
	private final HttpContext localContext;

	private ResponseParser responseParser;

	private HttpEntity entity;
	private HttpResponse response;
	private String content;
	
	public WebServiceFixture() {
		httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
		        ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		
		localContext = new BasicHttpContext();

		expectOutput("json");
	}
	
	public void expectOutput(final String format) {
		if ("json".equalsIgnoreCase(format)) {
			responseParser = new JsonResponseParser();
		} else if ("xml".equalsIgnoreCase(format)) {
			responseParser = new XmlResponseParser();
		}
	}
	
	public void httpGetRequest(final String url) throws Exception {
		HttpGet request = new HttpGet(FitNesseUtil.removeAnchorTag(url));
		addDefaultHeaders(request);
		executeRequest(request);
	}
	
	public void httpDeleteRequest(final String url) throws Exception {
        	HttpDelete request = new HttpDelete(FitNesseUtil.removeAnchorTag(url));
        	addDefaultHeaders(request);
        	executeRequest(request);
    	}

    private void addDefaultHeaders(final HttpMessage httpMessage) {
        httpMessage.setHeader("Accept", responseParser.acceptedMimeType());
    }

	public void setContent(final String content) throws UnsupportedEncodingException {
		entity = new StringEntity(content, "text/json", "UTF-8");
	}
	
	public void setPostParameters(final Map<String, String> parameters) throws UnsupportedEncodingException {
		System.out.println(parameters);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(parameters.size());
		for (Map.Entry<String, String> entry: parameters.entrySet()) {
			BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
			pairs.add(pair);
			System.out.println("post parameter pair: " + pair);
		}
		entity = new UrlEncodedFormEntity(pairs);
	}
	
	public void httpPostRequest(final String url) throws Exception {
		HttpPost request = new HttpPost(FitNesseUtil.removeAnchorTag(url));
		request.setEntity(entity);
        addDefaultHeaders(request);
		executeRequest(request);
	}

	private void executeRequest(final HttpUriRequest request) throws Exception {
		response = httpClient.execute(request, localContext);
		if (response.getEntity() != null) {
            content = EntityUtils.toString(response.getEntity());
            responseParser.parse(content);
        }
	}

	public int statusCode() {
		assertResponse();
		return response.getStatusLine().getStatusCode();
	}

	public String contentType() {
		return header("Content-Type");
	}

	public String header(final String header) {
		assertResponse();
		Header headerValue = response.getFirstHeader(header);
		return headerValue != null ? headerValue.getValue() : null;
	}

	public String content() {
		assertResponse();
		return content;
	}
	
	public boolean contentContains(final String substring) {
		assertResponse();
		return content.contains(substring);
	}

	public String path(final String path) {
		assertResponse();
		return responseParser.getValue(path);
	}
	
	// Object will be either Integer or String
	public Object pathCount(final String path) {
	    assertResponse();
	    return responseParser.getCount(path);
	}
	
	private void assertResponse() {
		if (response == null) {
			throw new AssertionError("First perform a Http GET or POST request");
		}
	}

	// For testing
	void setHttpClient(final HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	/**
	 * 'Namespaced' version of {@link #expectOutput(String)}.
	 * 
	 * @return
	 */
	public void webExpectOutput(final String format) {
		expectOutput(format);
	}
	
	/**
	 * 'Namespaced' version of {@link #content()}.
	 * 
	 * @return
	 */
	public String webContent() {
		return content;
	}
	
	/**
	 * 'Namespaced' version of {@link #path()}.
	 * 
	 * @return
	 */
	public String webPath(final String path) {
		return path(path);
	}
}
