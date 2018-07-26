/**
  * Copyright 2018 bejson.com 
  */
package com.besjon.pojo;

import java.util.Map;

/**
 * Auto-generated: 2018-06-21 10:30:36
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Parameters {

    private String in;
    private String name;
    private String description;
    private boolean required;
    private String type;
    private Map<String, String> schema;
    public void setIn(String in) {
         this.in = in;
     }
     public String getIn() {
         return in;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setDescription(String description) {
         this.description = description;
     }
     public String getDescription() {
         return description;
     }

    public void setRequired(boolean required) {
         this.required = required;
     }
     public boolean getRequired() {
         return required;
     }

    public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Map<String, String> getSchema() {
		return schema;
	}
	public void setSchema(Map<String, String> schema) {
		this.schema = schema;
	}

}