package com.iamds.rcms.client.baseClient;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ActionResponse {
	public Integer httpCode;
	public JSONObject jsonObject;
	public JSONArray jsonArray;
	
	public ActionResponse(Integer httpCode, JSONObject jsonObject, JSONArray jsonArray) {
		super();
		this.httpCode = httpCode;
		this.jsonObject = jsonObject;
		this.jsonArray = jsonArray;
	}
	
}
