package learnositysdk.request;

import org.json.JSONObject;

public class DataApiITCallback implements RequestCallback {

	@Override
	public void execute(JSONObject response) {
		try {
			JSONObject body = new JSONObject(response.getString("body"));
			if ((response.getInt("statusCode") == 200 && body.getJSONObject("meta").getBoolean("status") != true) ||
					(response.getInt("statusCode") != 200 && body.getJSONObject("meta").getBoolean("status") != false)) {
				System.out.println("Error in your code.");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
