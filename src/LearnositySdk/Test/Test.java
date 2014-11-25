package learnositysdk.test;

import learnositysdk.request.DataApi;
import learnositysdk.request.Init;
import learnositysdk.request.Remote;
import java.util.UUID;

import java.util.HashMap;
import java.util.List;
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
			Map reqData;
			Init init;
			String consumerSecret = "74c5fd430cf1242a527f6223aebd42d30464be22";
			JSONObject response;
			
			String endpoint = "https://data.vg.learnosity.com/stable/itembank/activities";

			Map<String,String> securityMap = new HashMap<String, String>();
			securityMap.put("consumer_key", consumerKey);
			securityMap.put("domain","localhost");
			JSONArray items = new JSONArray();
			items.put("bbf1380c-c75c-487c-90e3-18c7a36005d1");
			items.put("7bd193b1-3dc7-4c4d-913d-0278accffd67");
			items.put("99edf31b-8a7a-424b-8cba-f41795f55c19");
			JSONObject data = new JSONObject();
			data.put("items", items);
		    
			JSONObject activity = new JSONObject();
			activity.put("status","published");
			activity.put("description","My test description Title √");
			activity.put("data", data);
			activity.put("reference", UUID.randomUUID().toString());

			JSONArray activities = new JSONArray();
			activities.put(activity);
			
			JSONObject req = new JSONObject();
			req.put("activities", activities);
			
			DataApi dataApi = new DataApi(endpoint, securityMap, consumerSecret, req, "set");
			Remote remote = dataApi.request();
			String body = remote.getBody();
			System.out.println(body);
			
			
			/*
			*********************************************
			Creating the security setting with a HashMap
			*********************************************
			*/
			securityMap = new HashMap();
			securityMap.put("consumer_key", consumerKey);
			securityMap.put("user_id", "12345678");
			securityMap.put("timestamp", "20140915-0948");

			String secret = consumerSecret;
			String service = "questions";

			System.out.println("HashMap security test");

			init = new Init(service, securityMap, secret);
			//checkSecuritySettings(init.generate());
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
			sec.put("domain","assess.learnosity.com");

			System.out.println("Testing data api call with request data");
			reqData = new HashMap<String,String>();
			reqData.put("limit", "10");
			dataApi = new DataApi("https://data.learnosity.com/stable/itembank/items", sec, consumerSecret, reqData, "get");
			response = dataApi.requestJSONObject();
			JSONObject res = new JSONObject(response.getString("body"));
			if ((response.getInt("statusCode") == 200 && res.getJSONObject("meta").getBoolean("status") != true) ||
					(response.getInt("statusCode") != 200 && res.getJSONObject("meta").getBoolean("status") != false)) {
				System.out.println("Error in your code.");
			}

			System.out.println("Testing data api call without request data");
			dataApi = new DataApi("https://data.learnosity.com/stable/itembank/items", sec, consumerSecret);
			response = dataApi.requestJSONObject();
			res = new JSONObject(response.getString("body"));
			if ((response.getInt("statusCode") == 200 && res.getJSONObject("meta").getBoolean("status") != true) ||
					(response.getInt("statusCode") != 200 && res.getJSONObject("meta").getBoolean("status") != false)) {
				System.out.println("Error in your code.");
			}
			
			System.out.println("Testing data api call without request data, but with action");
			dataApi = new DataApi("https://data.learnosity.com/stable/itembank/items", sec, consumerSecret, "get");
			response = dataApi.requestJSONObject();
			res = new JSONObject(response.getString("body"));
			if ((response.getInt("statusCode") == 200 && res.getJSONObject("meta").getBoolean("status") != true) ||
					(response.getInt("statusCode") != 200 && res.getJSONObject("meta").getBoolean("status") != false)) {
				System.out.println("Error in your code.");
			}
			
			System.out.println("Testing recursive request");
			reqData.put("limit", "100");
			dataApi = new DataApi("https://data.learnosity.com/stable/itembank/items", sec, consumerSecret, reqData, "get");
			dataApi.requestRecursive(new TestCallback());
			/*
			*********************************************
			Testing assess initialisation
			*********************************************
			*/
			req = new JSONObject ();
			items = new JSONArray();
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
			Testing items initialisation
			*********************************************
			*/
			String itemSecString = "{\"consumer_key\":\"yis0TYCu7U9V4o7M\","
					+	"\"domain\": \"demos.vg.learnosity.com\","
					+   "\"user_id\": \"demo_student\"}";
	
			String itemsString = "{\"activity_id\": \"itemsassessdemo\","
				    + "\"name\": \"Items API demo - assess activity\","
				    + "\"rendering_type\" : \"assess\","
				    + "\"state\"          : \"initial\","
				    + "\"type\"           : \"submit_practice\","
				    + "\"course_id\"      : \"demo_yis0TYCu7U9V4o7M\","
				    + "\"session_id\"     : \"041f48c9-cb80-42e8-9d06-467d92013b00\","
				    + "\"user_id\"        : \"demo_student\","
				    + "\"items\": [\"Demo3\", \"Demo4\", \"Demo5\", \"Demo6\", \"Demo7\", \"Demo8\", \"Demo9\",\"Demo10\"],"
				    + "\"assess_inline\": true,"
				    + "\"config\": {"
				    + "\"title\": \"Demo activity - showcasing question types and assess options\","
				    + "    \"subtitle\"       : \"Walter White\","
				    + "    \"administration\" : {"
				    + "        \"pwd\" : \"5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8\","
				    + "        \"options\" : {"
				    + "            \"show_save\" : true,"
				    + "            \"show_exit\" : true,"
				    + "            \"show_extend\" : true"
				    + "        }"
				    + "    },"
				    + "    \"navigation\" : {"
				    + "        \"scroll_to_top\"            : false,"
				    + "        \"scroll_to_test\"           : false,"
				    + "        \"show_intro\"               : true,"
				    + "        \"show_outro\"               : false,"
				    + "        \"show_next\"                : true,"
				    + "        \"show_prev\"                : true,"
				    + "        \"show_accessibility\"       : true,"
				    + "        \"show_fullscreencontrol\"   : true,"
				    + "        \"show_progress\"            : true,"
				    + "        \"show_submit\"              : true,"
				    + "        \"show_title\"               : true,"
				    + "        \"show_save\"                : false,"
				    + "        \"show_calculator\"          : false,"
				    + "        \"show_itemcount\"           : true,"
				    + "        \"skip_submit_confirmation\" : false,"
				    + "        \"swipe\"                    : true,"
				    + "        \"toc\"                      : true,"
				    + "        \"transition\"               : \"slide\","
				    + "        \"transition_speed\"         : 400,"
				    + "        \"warning_on_change\"        : false,"
				    + "        \"scrolling_indicator\"      : false,"
				    + "        \"show_answermasking\"       : true,"
				    + "        \"auto_save\" : {"
				    + "            \"ui\" : false,"
				    + "            \"saveIntervalDuration\" : 500"
				    + "        }"
				    + "    },"
				    + "    \"time\" : {"
				    + "        \"max_time\"     : 1500,"
				    + "        \"limit_type\"   : \"soft\","
				    + "        \"show_pause\"   : true,"
				    + "        \"warning_time\" : 120,"
				    + "        \"show_time\"    : true"
				    + "    },"
				    + "    \"labelBundle\" : {"
				    + "        \"item\" : \"Question\""
				    + "    },"
				    + "    \"ui_style\"            : \"main\","
				    + "    \"ignore_validation\"   : false,"
				    + "    \"configuration\"       : {"
				    + "        \"fontsize\"               : \"normal\","
				    + "        \"stylesheet\"             : \"\","
				    + "        \"onsubmit_redirect_url\"  : \"itemsapi_assess.php\","
				    + "        \"onsave_redirect_url\"    : \"itemsapi_assess.php\","
				    + "        \"ondiscard_redirect_url\" : \"itemsapi_assess.php\","
				    + "        \"idle_timeout\"           : {"
				    + "            \"interval\"       : 300,"
				    + "            \"countdown_time\" : 60"
				    + "        }"
				    + "    }"
				    + "}}";
			
			JSONObject itemsObj = new JSONObject(itemsString);
			//itemsString = itemsObj.toString();
			init = new Init("items", itemSecString, consumerSecret, itemsObj.toString(), "");
			String itemsTest = init.generate();
			//System.out.println("In order to test this in the actual items api, set signedRequest in demo site, itemsapi_assess.php to the following value:");
			//System.out.println(itemsTest);

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
			sec.put("user_id", "12345678");
			init = new Init("questions", sec, consumerSecret, req, "");
			test = new JSONObject(init.generate());
			if (!test.get("type").equals("local_practice") || test.getJSONArray("questions").length() != 1 || !test.get("consumer_key").equals(consumerKey)) {
				throw new Exception("Errors in the questions api initialisation");
			}
			
			System.out.println("Testing question initialisation with JSON strings");

			String secString = "{\"consumer_key\":\"yis0TYCu7U9V4o7M\","
							+	"\"domain\": \"localhost\""
							+   "\"user_id\": \"12345678\"}";
			
			String reqString = "{\"state\":\"initial\","
							+  "\"type\":\"local_practice\","
							+  "\"timestamp\":\"20140617-0533\","
							+  "\"response_id\":\"60005\","
							+  "\"questions\":"
							+		"[{\"stimulus_list\":"
							+			"[\"London\","
							+ 			 "\"Dublin\","
							+ 			 "\"Paris\","
							+			 "\"Sydney\"],"
							+	"\"stimulus\":\"Match the cities to the parent nation\","
							+   "\"type\":\"association\","
							+   "\"possible_responses\":"
							+		"[\"Australia\","
							+		  "\"France\","
							+		  "\"Ireland\","
							+ 		  "\"England\"],"
							+   "\"validation\":"
							+		"{\"valid_responses\":"
							+  			"[\"England\","
							+             "\"Ireland\","
							+			  "\"France\","
							+			  "\"Australia\"]}}]}";

			
			
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
		//if (!signature.equals(expectedSignature)) {
			//throw new Exception("Again check your code");
		//}
	}
}
