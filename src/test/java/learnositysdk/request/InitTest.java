package learnositysdk.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import org.json.JSONObject;
import org.json.JSONArray;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InitTest {

	static private String consumerKey = "yis0TYCu7U9V4o7M";
	static private String consumerSecret = "74c5fd430cf1242a527f6223aebd42d30464be22";
	static private String expectedSignature = "$02$dfc7ff0c50de11a52d9efb2a2cc03588610f187ccf15136ffd06a760857ef0ab";

	private JSONObject securityObj;
	private JSONObject request;
	private Init init;
	private JSONObject signedRequest;

	@BeforeEach
	public void setUp()
		throws java.lang.Exception
	{
		securityObj = new JSONObject();
		securityObj.put("consumer_key", consumerKey);
		securityObj.put("user_id", "$ANONYMIZED_USER_ID");
		securityObj.put("timestamp", "20140612-0438");

		Init.disableTelemetry();

		request = new JSONObject ();
	}

	@Test
	public void testInitGenerate()
		throws java.lang.Exception
	{
		System.out.println("Init: Generate");

		init = new Init("questions", securityObj, consumerSecret);

		assertSecurityPacket(init.generate());
		assertSignature(init.generateSignature());
	}

	@Test
	public void testInitGenerateFromString()
		throws java.lang.Exception
	{
		System.out.println("Init: Generate from String");

		String securityString = securityObj.toString();
		init = new Init("questions", securityString, consumerSecret);

		assertSecurityPacket(init.generate());
		assertSignature(init.generateSignature());
	}

	@Test
	public void testInitGenerateFromHashMap()
		throws java.lang.Exception
	{
		System.out.println("Init: Generate from HashMap");

		Map<String,String> securityMap = new HashMap<String,String>();
		Iterator<?> keys = securityObj.keys();
		while( keys.hasNext() ) {
			String key = (String)keys.next();
			securityMap.put(key, securityObj.getString(key));
		}

		init = new Init("questions", securityMap, consumerSecret);

		assertSecurityPacket(init.generate());
		assertSignature(init.generateSignature());
	}

	@Test
	public void testInitAssessGenerate()
		throws java.lang.Exception
	{
		System.out.println("Init Assess: Generate");

		JSONArray responseIds = new JSONArray();
		responseIds.put(0, "Demo123");

		JSONObject item = new JSONObject();
		item.put("reference", "Demo3");
		item.put("content", "<p>HI</p>");
		item.put("response_ids", responseIds);

		JSONArray items = new JSONArray();
		items.put(0, item);

		request.put("items", items);

		securityObj.put("domain","demos.learnosity.com");

		init = new Init("assess", securityObj, consumerSecret, request);

		JSONObject signedRequest = new JSONObject(init.generate());

		assertEquals("Demo3",
			signedRequest.getJSONArray("items").getJSONObject(0).get("reference"),
			"Errors in the Assess initialisation");
	}

	@Test
	public void testInitItemsGenerate()
		throws java.lang.Exception
	{
		System.out.println("Init Items: Generate");

		String expectedSignature = "$02$b73940dc5d8841e20aa271323ffdbd1ec6df700eeaa7ec02928251150ab2529c";
		String itemsString = "{\"user_id\":\"$ANONYMIZED_USER_ID\",\"rendering_type\":\"assess\",\"name\":\"Items API demo - assess activity demo\",\"state\":\"initial\",\"activity_id\":\"items_assess_demo\",\"session_id\":\"demo_session_uuid\",\"type\":\"submit_practice\",\"config\":{\"configuration\":{\"responsive_regions\":true},\"navigation\":{\"scrolling_indicator\":true},\"regions\":\"main\",\"time\":{\"show_pause\":true,\"max_time\":300},\"title\":\"ItemsAPI Assess Isolation Demo\",\"subtitle\":\"Testing Subtitle Text\"},\"items\":[\"Demo3\"]}";

		request = new JSONObject(itemsString);

		securityObj.put("domain","demos.learnosity.com");

		init = new Init("items", securityObj, consumerSecret, request);
		JSONObject signedRequest = new JSONObject(init.generate());

		assertEquals(expectedSignature,
			signedRequest.getJSONObject("security").getString("signature"),
			"Errors in the Items initialisation");
	}

	// XXX: This test is skipped as different JDKs reorder JSON arrays in different ways.
	// The signature for the final string will still be valid, but we cannot easily
	// encode this uncertainty is a practical test across all JDKs.
	@Disabled
	@Test
	public void testInitItemsComplexGenerate()
		throws java.lang.Exception
	{
		System.out.println("Init Items: Generate");

		// Valid signature, as generated from the Python SDK,
		// obtainable with the Oracle SDK
		String expectedSignature = "c4ef178bc2ab3666e8ca6055e132dba7f670efaf57204ef08e0773387025f96f";
		String itemsString = "{\"activity_id\": \"itemsassessdemo\","
			+ "\"name\": \"Items API demo - assess activity\","
			+ "\"rendering_type\" : \"assess\","
			+ "\"state\"          : \"initial\","
			+ "\"type\"           : \"submit_practice\","
			+ "\"course_id\"      : \"demo_yis0TYCu7U9V4o7M\","
			+ "\"session_id\"     : \"041f48c9-cb80-42e8-9d06-467d92013b00\","
			+ "\"user_id\"        : \"$ANONYMIZED_USER_ID\","
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
		request = new JSONObject(itemsString);

		securityObj.put("domain","demos.learnosity.com");

		init = new Init("items", securityObj, consumerSecret, request);
		String itemsTest = init.generate();
		JSONObject signedRequest = new JSONObject(init.generate());

		assertEquals(expectedSignature,
			signedRequest.getJSONObject("security").getString("signature"),
			"Errors in the Items initialisation");
	}

	/* XXX: This is quite redundant with testInitQuestionsGenerate and
	 * testInitGenerateFromString
	 */
	@Test
	public void testInitQuestionsGenerate()
		throws java.lang.Exception
	{
		System.out.println("Init Questions: Generate");

		JSONArray stimList = new JSONArray();
		stimList.put(0, "London");
		stimList.put(1, "Dublin");
		stimList.put(2, "Paris");
		stimList.put(3, "Sydney");

		JSONArray possibleResp = new JSONArray();
		possibleResp.put(0, "Australia");
		possibleResp.put(1, "France");
		possibleResp.put(2, "Ireland");
		possibleResp.put(3, "England");

		JSONArray validResp = new JSONArray();
		validResp.put(0, "England");
		validResp.put(1, "Ireland");
		validResp.put(2, "France");
		validResp.put(3, "Australia");

		JSONObject validation = new JSONObject();
		validation.put("valid_responses", validResp);

		JSONObject question = new JSONObject();
		question.put("response_id", "60005");
		question.put("type", "association");
		question.put("stimulus", "Match the cities to the parent nation");

		question.put("stimulus_list", stimList);
		question.put("possible_responses", possibleResp);
		question.put("validation", validation);

		JSONArray questions = new JSONArray();
		questions.put(0, question);

		request = new JSONObject();
		request.put("type", "local_practice");
		request.put("state", "initial");
		request.put("questions", questions);

		securityObj.put("user_id", "$ANONYMIZED_USER_ID");

		init = new Init("questions", securityObj, consumerSecret, request);
		JSONObject signedRequest = new JSONObject(init.generate());

		assertEquals("local_practice",
			signedRequest.get("type"),
			"Error in the Questions API initialisation: type");

		assertEquals(1,
			signedRequest.getJSONArray("questions").length(),
			"Error in the Questions API initialisation: number of questions");

		assertEquals(consumerKey,
			signedRequest.get("consumer_key"),
			"Error in the Questions API initialisation: consumer key");
	}

	/* XXX: This is quite redundant with testInitQuestionsGenerate and
	 * testInitGenerateFromString
	 */
	@Test
	public void testInitQuestionsGenerateFromString()
		throws java.lang.Exception
	{
		System.out.println("Init Questions: Generate from string");

		String reqString = "{\"state\":\"initial\","
			+  "\"type\":\"local_practice\","
			+  "\"response_id\":\"60005\","
			+  "\"questions\":"
			+		"[{\"stimulus_list\":"
			+			"[\"London\","
			+			 "\"Dublin\","
			+			 "\"Paris\","
			+			 "\"Sydney\"],"
			+	"\"stimulus\":\"Match the cities to the parent nation\","
			+   "\"type\":\"association\","
			+   "\"possible_responses\":"
			+		"[\"Australia\","
			+		  "\"France\","
			+		  "\"Ireland\","
			+		  "\"England\"],"
			+   "\"validation\":"
			+		"{\"valid_responses\":"
			+			"[\"England\","
			+             "\"Ireland\","
			+			  "\"France\","
			+			  "\"Australia\"]}}]}";

		init = new Init("questions", securityObj, consumerSecret, reqString);
		signedRequest = new JSONObject(init.generate());

		assertEquals("local_practice",
			signedRequest.get("type"),
			"Errors in the Questions API initialisation: type");

		assertEquals(1,
			signedRequest.getJSONArray("questions").length(),
			"Errors in the Questions API initialisation: number of questions");

		assertEquals(consumerKey,
			signedRequest.get("consumer_key"),
			"Errors in the Questions API initialisation: consumer key");
	}

	@Test
	public void testInitEventsGenerate()
		throws java.lang.Exception
	{
		System.out.println("Init Events: Generate");

		JSONArray users = new JSONArray();
		users.put("$ANONYMIZED_USER_ID_1");
		users.put("$ANONYMIZED_USER_ID_2");

		request = new JSONObject();
		request.put("users", users);

		init = new Init("events", securityObj, consumerSecret, request);
		signedRequest = new JSONObject(init.generate());
		JSONObject signedUsers = signedRequest.getJSONObject("config")
			.getJSONObject("users");

		assertTrue(signedRequest.has("config"), "Error in the Events API initialisation: config");
		assertTrue(signedRequest.getJSONObject("config").has("users"),
			"Error in the Events API initialisation: users");
		assertTrue(signedUsers.has("$ANONYMIZED_USER_ID_1"),
			"Error in the Events API initialisation, missing user: $ANONYMIZED_USER_ID_1");
		assertEquals("$02$e3d4ba09b1421b3ec5345d3dc5f68d551dd13e5d0c5c728f2a7b5034b9e8dacd",
			signedUsers.getString("$ANONYMIZED_USER_ID_1"),
			"Error in the Events API initialisation, invalid signature: $ANONYMIZED_USER_ID_1");
		assertTrue(signedUsers.has("$ANONYMIZED_USER_ID_2"),
				"Error in the Events API initialisation, missing user: $ANONYMIZED_USER_ID_2");
		assertEquals("$02$dbe6dae7dfb87669bff36107e83b814e48dbc7d1d9d80dd679efe919480df0d6",
			signedUsers.getString("$ANONYMIZED_USER_ID_2"),
			"Error in the Events API initialisation, invalid signature: $ANONYMIZED_USER_ID_2");
	}

	@Test
	public void testDataApiGenerate()
		throws java.lang.Exception
	{
		String[][] testCases = {
			{
				"get",
				"$02$e19c8a62fba81ef6baf2731e2ab0512feaf573ca5ca5929c2ee9a77303d2e197"
			},
			{
				"post",
				"$02$9d1971fb9ac51482f7e73dcf87fc029d4a3dfffa05314f71af9d89fb3c2bcf16"
			}
		};

		for (String[] testCase: testCases) {
			String action = testCase[0];
			String expectedSignature = testCase[1];

			System.out.println("Data API " + action + ": Generate");

			String reqString = "{\"limit\":100}";
			String timestamp = "20140626-0528";

			// Ensure no altering request even if telemetry is enabled
			Init.enableTelemetry();

			securityObj.put("domain", "localhost");
			securityObj.put("timestamp", timestamp);
			securityObj.remove("user_id");

			init = new Init("data", securityObj, consumerSecret, reqString);
			init.setAction(action);
			signedRequest = new JSONObject(init.generate());

			assertEquals(reqString,
				init.getRequestString(),
				"Error in Data API signing, request string altered when it must not be");

			assertEquals(expectedSignature,
				signedRequest.getString("signature"),
				"Error in the Data API signing, invalid signature");
		}
	}

	@Test
	public void testJsonSlashEscaping()
        throws java.lang.Exception
    {
        System.out.println("Init: Test that forward slashes are not escaped");

        String expectedSignature = "$02$5213b43f18af4be5abe7f9457f4f73ee2f6cc82ef9573cc4e33ec898b97f8510";
        String expectedSubstring = "<h1>Items API demo</h1>";
        String itemsString = "{\"user_id\":\"$ANONYMIZED_USER_ID\",\"rendering_type\":\"assess\",\"name\":\"" + expectedSubstring + " - assess activity demo\",\"state\":\"initial\",\"activity_id\":\"items_assess_demo\",\"session_id\":\"demo_session_uuid\",\"type\":\"submit_practice\",\"items\":[\"Demo3\"]}";

        request = new JSONObject(itemsString);

        securityObj.put("domain","demos.learnosity.com");

        init = new Init("items", securityObj, consumerSecret, request);
        String signedRequest = init.generate();
        JSONObject signedReqObject = new JSONObject(signedRequest);

        assertTrue(signedRequest.contains(expectedSubstring),
			"Expected substring was not found, make sure slashes are not escaped"
        );

        assertEquals(expectedSignature,
            signedReqObject.getJSONObject("security").getString("signature"),
			"Invalid signature generated"
        );
    }

	@Test
	public void testJsonUnicodeEscaping()
			throws java.lang.Exception
	{
		System.out.println("Init: Test that unicode characters are not escaped");

		String expectedEnDashSignature = "$02$622fcdb64beeb4b58939b5ac31463c80aea5419e50649b5077c2730e951675f2";
		String request = "{\"endash\":\"–\",\"slash\":\"/\",\"slash-back\":\"\\\\\"}";

		securityObj.put("domain","demos.learnosity.com");

		init = new Init("items", securityObj, consumerSecret, request);
		String signedRequest = init.generate();
		JSONObject signedReqObject = new JSONObject(signedRequest);

		assertSecurityPacket(signedReqObject.getJSONObject("security").toString());

		assertEquals(expectedEnDashSignature,
				signedReqObject.getJSONObject("security").getString("signature"),
				"Invalid signature generated"
		);
	}

	private static void assertSecurityPacket (String securityObj)
		throws org.json.JSONException
	{
		JSONObject security = new JSONObject(securityObj);

		assertEquals(consumerKey, security.getString("consumer_key"), "Unexpected consumer key");
		assertEquals("$ANONYMIZED_USER_ID", security.getString("user_id"), "Unexpected user id");
		assertEquals("20140612-0438", security.getString("timestamp"), "Unexpected timestamp");
	}

	private void assertSignature (String signature)
	{
		assertEquals(expectedSignature, signature, "Unexpected signature");
	}
}
