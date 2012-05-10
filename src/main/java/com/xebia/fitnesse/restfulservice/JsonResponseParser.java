package com.xebia.fitnesse.restfulservice;

import java.io.File;
import java.io.IOException;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;

public class JsonResponseParser implements ResponseParser {

	private String content;
	
    @Override
	public void parse(final String content) {
		this.content = content;
	}

    @Override
	public String getValue(final String path) {
		try {
		    return JsonPath.read(content, path).toString();
		} catch (InvalidPathException e) {
			return error(e.getMessage(), path);
		} catch (IndexOutOfBoundsException e) {
			return error("index out of bounds", path);
		}
	}

    @Override
    public Object getCount(final String path) {
        try {
             Object results = JsonPath.read(content, path);
             if (results instanceof JSONArray) {
                 return ((JSONArray) results).size();
             } else if (results instanceof JSONObject) {
                 return ((JSONObject) results).size();
             }
             return results != null ? 1 : 0;
        } catch (InvalidPathException e) {
            return error(e.getMessage(), path);
        } catch (IndexOutOfBoundsException e) {
            return error("index out of bounds", path);
        }
    }

	private String error(final String message, final String path) {
        return "Error: " + message + " (" + path + ")";
    }

    @Override
	public void parse(final File file) throws IOException {
		parse(FitNesseUtil.readFile(file));
	}

    @Override
    public String acceptedMimeType() {
        return "application/json";
    }
}
