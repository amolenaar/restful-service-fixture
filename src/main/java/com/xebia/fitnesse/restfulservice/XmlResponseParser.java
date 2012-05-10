package com.xebia.fitnesse.restfulservice;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.minidev.json.JSONArray;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XmlResponseParser implements ResponseParser {

	DocumentBuilderFactory documentBuilderFactory;
    private final XPathFactory xpathFactory;
    
	private Document document;

	public XmlResponseParser() {
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		// Not namespace aware by default. This simplifies stuff,
		// but can break complex xml
		documentBuilderFactory.setNamespaceAware(false);
		xpathFactory = XPathFactory.newInstance();
	}
	
	@Override
	public void parse(final String content) throws Exception {
		DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
		document = builder.parse(new ByteArrayInputStream(content.getBytes("UTF-8")));
	}

	@Override
	public void parse(final File file) throws Exception {
		DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
		document = builder.parse(file);
	}

	@Override
	public String getValue(final String path) {
	    XPath xpath = xpathFactory.newXPath();
	    NodeList nodes;
		try {
			XPathExpression expr = xpath.compile(path);
		    nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}

		switch (nodes.getLength()) {
		case 0:
			return null;
		case 1:
			return nodes.item(0).getNodeValue();
	    default:
		    List<String> vals = new ArrayList<String>();
		    for (int i = 0; i < nodes.getLength(); i++) {
		        vals.add(nodes.item(i).getNodeValue()); 
		    }
		    return JSONArray.toJSONString(vals);
		}
	}

    @Override
    public Object getCount(final String path) {
        // TODO Auto-generated method stub
        throw new RuntimeException("getCount not implemented");
    }

    @Override
    public String acceptedMimeType() {
        return "text/xml, application/xml";
    }

}
