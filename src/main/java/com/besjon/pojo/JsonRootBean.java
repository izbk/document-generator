/**
  * Copyright 2018 bejson.com 
  */
package com.besjon.pojo;

import java.util.List;
import java.util.Map;

/**
 * Auto-generated: 2018-06-21 10:30:36
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

    private String swagger;
    private Info info;
    private String host;
    private String basePath;
	private List<Tags> tags;
	private List<Map<String,Path>> paths;
	private List<Map<String, Object>> definitions;

	public String getSwagger() {
		return swagger;
	}

	public void setSwagger(String swagger) {
		this.swagger = swagger;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public List<Tags> getTags() {
		return tags;
	}

	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}

	public List<Map<String, Path>> getPaths() {
		return paths;
	}

	public void setPaths(List<Map<String, Path>> paths) {
		this.paths = paths;
	}

	public List<Map<String, Object>> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(List<Map<String, Object>> definitions) {
		this.definitions = definitions;
	}
	
	public static void main(String[] args) {
		String string = "#/definitions/Result«PageInfo«Device»»";
		System.out.println(string.replaceAll("#/definitions/Result«", "").replaceFirst("»", ""));
	}

}