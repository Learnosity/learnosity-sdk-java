package learnositysdk.test;

import learnositysdk.request.DataApi;
import learnositysdk.request.Init;
import learnositysdk.request.Remote;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;

public class Test {
	
	static private String consumerKey = "yis0TYCu7U9V4o7M";
	static private String expectedSignature = "e9cd04b624d1dbe89fd4cad0a447f485e0fcec1392cbd3e2841826a954cc4e8e";
	private Remote remote;
	private Init init;
	
	public static void main (String[] args)
	{
		try {
			Remote remote = new Remote();
			Map reqData;
			Init init;
			String consumerSecret = "74c5fd430cf1242a527f6223aebd42d30464be22";
			
			/*
			*********************************************
			Creating the security setting with a HashMap
			*********************************************
			*/
			HashMap securityMap = new HashMap();
			securityMap.put("consumer_key", consumerKey);
			securityMap.put("user_id", "12345678");
			securityMap.put("timestamp", "20140612-0438");

			String secret = consumerSecret;
			String service = "questions";

			System.out.println("HashMap security test");

			init = new Init(service, securityMap, secret);
			checkSecuritySettings(init.generate());
			checkSignature(init.generateSignature());

			/*
			*********************************************
			Creating the security settings with a JSONObject
			*********************************************
			*/
			JSONObject securityObj = new JSONObject();
			securityObj.put("consumer_key", consumerKey);
			securityObj.put("user_id", "12345678");
			securityObj.put("timestamp", "20140612-0438");
			String securityString = securityObj.toString();
			System.out.println("JSONObject security test");
			init = new Init(service, securityObj, secret);
			checkSecuritySettings(init.generate());
			checkSignature(init.generateSignature());
			
			/*
			*********************************************
			Creating the security settings with a String
			*********************************************
			*/
			System.out.println("String security test");
			init = new Init(service, securityString, secret);
			checkSecuritySettings(init.generate());
			checkSignature(init.generateSignature());

			/*
			*********************************************
			Testing call to the data api
			*********************************************
			*/	
			Map<String,String> sec = new HashMap<String, String>();
			sec.put("consumer_key", consumerKey);
			sec.put("domain","assess.vg.learnosity.com");

			System.out.println("Testing data api call with request data");
			reqData = new HashMap<String,String>();
			reqData.put("limit", "10");
			DataApi dataApi = new DataApi("https://data.vg.learnosity.com/latest/itembank/items", sec, consumerSecret, reqData, "get");
			remote = dataApi.request();
			JSONObject res = new JSONObject(remote.getBody());
			if ((remote.getStatusCode() == 200 && res.getJSONObject("meta").getBoolean("status") != true) ||
					(remote.getStatusCode() != 200 && res.getJSONObject("meta").getBoolean("status") != false)) {
				System.out.println("Error in your code.");
			}

			System.out.println("Testing data api call without request data");
			dataApi = new DataApi("https://data.vg.learnosity.com/latest/itembank/items", sec, consumerSecret);
			remote = dataApi.request();
			res = new JSONObject(remote.getBody());
			if ((remote.getStatusCode() == 200 && res.getJSONObject("meta").getBoolean("status") != true) ||
					(remote.getStatusCode() != 200 && res.getJSONObject("meta").getBoolean("status") != false)) {
				System.out.println("Error in your code.");
			}
			
			System.out.println("Testing data api call without request data, but with action");
			dataApi = new DataApi("https://data.vg.learnosity.com/latest/itembank/items", sec, consumerSecret, "get");
			remote = dataApi.request();
			res = new JSONObject(remote.getBody());
			if ((remote.getStatusCode() == 200 && res.getJSONObject("meta").getBoolean("status") != true) ||
					(remote.getStatusCode() != 200 && res.getJSONObject("meta").getBoolean("status") != false)) {
				System.out.println("Error in your code.");
			}
			
			/*
			*********************************************
			Testing assess initialisation
			*********************************************
			*/
			JSONObject req = new JSONObject ();
			JSONArray items = new JSONArray();
			JSONObject item = new JSONObject();
			item.put("reference", "Demo3");
			item.put("content", "<p>HI</p>");
			JSONArray responseIds = new JSONArray();
			responseIds.put(0, "Demo123");
			item.put("response_ids", responseIds);
			items.put(0, item);
			req.put("items", items);

			System.out.println("Testing assess initialisation");
			init = new Init("assess", sec, consumerSecret, req, "");
			JSONObject test = new JSONObject(init.generate());
			if (!test.getJSONArray("items").getJSONObject(0).get("reference").equals("Demo3")) {
				throw new Exception("Errors in the assess initialisation");
			}

			/*
			*********************************************
			Testing question initialisation
			*********************************************
			*/
			req = new JSONObject();
			
			// Create some data for the question api
			req.put("type", "local_practice");
			req.put("state", "initial");

			// Create a questions JSONArrray
			JSONArray questions = new JSONArray();

			// Create a question
			JSONObject question = new JSONObject();
			question.put("response_id", "60005");
			question.put("type", "association");
			question.put("stimulus", "Match the cities to the parent nation");

			// Add stimulus list
			JSONArray stimList = new JSONArray();
			stimList.put(0, "London");
			stimList.put(1, "Dublin");
			stimList.put(2, "Paris");
			stimList.put(3, "Sydney");
			question.put("stimulus_list", stimList);

			// Add possible responses
			JSONArray possibleResp = new JSONArray();
			possibleResp.put(0, "Australia");
			possibleResp.put(1, "France");
			possibleResp.put(2, "Ireland");
			possibleResp.put(3, "England");
			question.put("possible_responses", possibleResp);

			// Add validation
			JSONObject validation = new JSONObject();
			JSONArray validResp = new JSONArray();
			validResp.put(0, "England");
			validResp.put(1, "Ireland");
			validResp.put(2, "France");
			validResp.put(3, "Australia");
			validation.put("valid_responses", validResp);
			question.put("validation", validation);

			// Add questions to questions array
			questions.put(0, question);

			// Finally add questions to request
			req.put("questions", questions);

			System.out.println("Testing question initialisation");
			init = new Init("questions", sec, consumerSecret, req, "");
			test = new JSONObject(init.generate());

			if (!test.get("type").equals("local_practice") || test.getJSONArray("questions").length() != 1 || !test.get("consumer_key").equals(consumerKey)) {
				throw new Exception("Errors in the questions api initialisation");
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	private static void checkSecuritySettings (String sec) throws Exception
	{
		JSONObject security = new JSONObject(sec);
		
		if (!(security.getString("consumer_key").equals(consumerKey) &&
			security.getString("user_id").equals("12345678") &&
			security.getString("timestamp").equals("20140612-0438") &&
			security.getString("signature").equals(expectedSignature))) {
	
			throw new Exception("Idiot, check your code");
		}
	}
	
	private static void checkSignature (String signature) throws Exception
	{
		if (!signature.equals(expectedSignature)) {
			throw new Exception("Again check your code");
		}
	}
}
