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
public class Get {

    private List<String> tags;
    private String summary;
    private String operationId;
    private List<String> consumes;
    private List<String> produces;
    private List<Parameters> parameters;
    private List<Map<String, Response>> responses;
    public void setTags(List<String> tags) {
         this.tags = tags;
     }
     public List<String> getTags() {
         return tags;
     }

    public void setSummary(String summary) {
         this.summary = summary;
     }
     public String getSummary() {
         return summary;
     }

    public void setOperationId(String operationId) {
         this.operationId = operationId;
     }
     public String getOperationId() {
         return operationId;
     }

    public void setConsumes(List<String> consumes) {
         this.consumes = consumes;
     }
     public List<String> getConsumes() {
         return consumes;
     }

    public void setProduces(List<String> produces) {
         this.produces = produces;
     }
     public List<String> getProduces() {
         return produces;
     }

    public void setParameters(List<Parameters> parameters) {
         this.parameters = parameters;
     }
     public List<Parameters> getParameters() {
         return parameters;
     }

    public void setResponses(List<Map<String, Response>> responses) {
         this.responses = responses;
     }
     public List<Map<String, Response>> getResponses() {
         return responses;
     }

}