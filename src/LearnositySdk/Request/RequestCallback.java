package learnositysdk.request;

import org.json.JSONObject;

public interface RequestCallback {
	
	public void execute (JSONObject response);

}
