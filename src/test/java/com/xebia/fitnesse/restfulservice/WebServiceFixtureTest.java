package com.xebia.fitnesse.restfulservice;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Before;
import org.junit.Test;

public class WebServiceFixtureTest {

	private WebServiceFixture service;
	private HttpClient httpClient;

	@Before
	public void setUp() {
		service = new WebServiceFixture();
		httpClient = mock(HttpClient.class);
		service.setHttpClient(httpClient);
	}
	
	@Test(expected=AssertionError.class)
	public void testStatusCodeWithoutRequest() {
		new WebServiceFixture().statusCode();
	}
	
	@Test(expected=AssertionError.class)
	public void testContentTypeHeaderWithoutRequest() {
		new WebServiceFixture().contentType();
	}
	
	@Test
	public void canSendSimpleGetRequest() throws Exception {
		BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
		response.setEntity(new StringEntity("{ \"foo\": \"bar\" }"));
		stub(httpClient.execute(any(HttpGet.class))).toReturn(response);
		
		service.httpGetRequest("http://localhost");
		
		assertEquals("{ \"foo\": \"bar\" }", service.content());
	}
}
