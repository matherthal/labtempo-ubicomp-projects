package br.uff.tempo.middleware.comm.current.api;

import java.util.List;

public class JSONRPC {
	private String jsonrpc = "2.0";
	private String id;
	
	//request
	private String method;
	private List<String> params;
	private List<String> types;
	
	//response
	private String response;	
	private String responseType;

	
	public JSONRPC(String id, String method, List<String> params, List<String> types) {
		this.id = id;
		this.method = method;
		this.params = params;
		this.types = types;
	}
	
	public JSONRPC(String id, String response, String responseType) {
		this.id = id;
		this.response = response;
		this.responseType = responseType;
	}
	
	public JSONRPC(String id) {
		this.id = id;
	}

	public String getJsonrpc() {
		return jsonrpc;
	}
	
	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}
	
	public String getMethod() {
		return method;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
	
	public List<String> getParams() {
		return params;
	}
	
	public void setParams(List<String> params) {
		this.params = params;
	}
	
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public List<String> getTypes() {
		return types;
	}
	
	public void setTypes(List<String> types) {
		this.types = types;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
