package com.learnosity.sdk.request;

import org.json.JSONObject;

public interface RequestCallback {
	
	void execute (JSONObject response);

}
