package learnositysdk.request;

import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InitTest
	extends TestCase {

	static private String consumerKey = "yis0TYCu7U9V4o7M";
	static private String consumerSecret = "74c5fd430cf1242a527f6223aebd42d30464be22";
	static private String expectedSignature = "e9cd04b624d1dbe89fd4cad0a447f485e0fcec1392cbd3e2841826a954cc4e8e";

	private JSONObject securityObj;
	private JSONObject request;
	private Init init;
	private JSONObject signedRequest;

	@Override
	protected void setUp()
		throws java.lang.Exception
	{
		securityObj = new JSONObject();
		securityObj.put("consumer_key", consumerKey);
		securityObj.put("user_id", "12345678");
		securityObj.put("timestamp", "20140612-0438");

		request = new JSONObject ();
	}

	public void testInitGenerate()
		throws java.lang.Exception
	{
		System.out.println("Init: Generate");

		init = new Init("questions", securityObj, consumerSecret);

		assertSecurityPacket(init.generate());
		assertSignature(init.generateSignature());
	}

	public void testInitGenerateAsJsonObject() throws Exception
	{
		System.out.println("Init: Generate as json object");

		init = new Init("questions", securityObj, consumerSecret);
		assertSecurityPacket(init.generateAsJsonObject().toString());
	}

	public void testInitGenerateFromString()
		throws java.lang.Exception
	{
		System.out.println("Init: Generate from String");

		String securityString = securityObj.toString();
		init = new Init("questions", securityString, consumerSecret);

		assertSecurityPacket(init.generate());
		assertSignature(init.generateSignature());
	}

	public void testInitGenerateFromHashMap()
		throws java.lang.Exception
	{
		System.out.println("Init: Generate from HashMap");

		Map<String,String> securityMap = new HashMap();
		Iterator<?> keys = securityObj.keys();
		while( keys.hasNext() ) {
			String key = (String)keys.next();
			securityMap.put(key, securityObj.getString(key));
		}

		init = new Init("questions", securityMap, consumerSecret);

		assertSecurityPacket(init.generate());
		assertSignature(init.generateSignature());
	}

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
		assertEquals("Errors in the Assess initialisation",
				"Demo3",
				signedRequest.getJSONArray("items").getJSONObject(0).get("reference")
		);
	}

	public void testInitAssessGenerateAsObject()
			throws java.lang.Exception
	{
		System.out.println("Init Assess: Generate As Object");

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

		JSONObject signedRequest = init.generateAsJsonObject();
		assertEquals("Errors in the Assess initialisation",
				"Demo3",
				signedRequest.getJSONArray("items").getJSONObject(0).get("reference")
		);
	}

	public void testInitItemsGenerate()
		throws java.lang.Exception
	{
		System.out.println("Init Items: Generate");

		String expectedSignature = "1e2e42c037e2536d4252d18bd6515ea8bee7ae6f70d2f7c6156c923605115113";
		String itemsString = "{\"limit\":50}";

		request = new JSONObject(itemsString);

		securityObj.put("domain","demos.learnosity.com");

		init = new Init("items", securityObj, consumerSecret, request);
		String itemsTest = init.generate();
		JSONObject signedRequest = new JSONObject(init.generate());

		assertEquals("Errors in the Items initialisation",
				expectedSignature,
				signedRequest.getJSONObject("security").getString("signature")
			    );
	}

	// XXX: This test is skipped as different JDKs reorder JSON arrays in different ways.
	// The signature for the final string will still be valid, but we cannot easily
	// encode this uncertainty is a practical test across all JDKs.
	public void SKIPPEDtestInitItemsComplexGenerate()
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
		request = new JSONObject(itemsString);

		securityObj.put("domain","demos.learnosity.com");

		init = new Init("items", securityObj, consumerSecret, request);
		String itemsTest = init.generate();
		JSONObject signedRequest = new JSONObject(init.generate());

		assertEquals("Errors in the Items initialisation",
				expectedSignature,
				signedRequest.getJSONObject("security").getString("signature")
			    );
	}

	/* XXX: This is quite redundant with testInitQuestionsGenerate and
	 * testInitGenerateFromString
	 */
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

		securityObj.put("user_id", "12345678");

		init = new Init("questions", securityObj, consumerSecret, request);
		JSONObject signedRequest = new JSONObject(init.generate());

		assertEquals("Error in the Questions API initialisation: type",
				"local_practice", signedRequest.get("type"));
		assertEquals("Error in the Questions API initialisation: number of questions",
				1, signedRequest.getJSONArray("questions").length());
		assertEquals("Error in the Questions API initialisation: consumer key",
				consumerKey, signedRequest.get("consumer_key"));
	}

	/* XXX: This is quite redundant with testInitQuestionsGenerate and
	 * testInitGenerateFromString
	 */
	public void testInitQuestionsGenerateFromString()
		throws java.lang.Exception
	{
		System.out.println("Init Questions: Generate from string");

		String reqString = "{\"state\":\"initial\","
			+  "\"type\":\"local_practice\","
			+  "\"timestamp\":\"20140617-0533\","
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

		assertEquals("Errors in the Questions API initialisation: type",
				"local_practice", signedRequest.get("type"));
		assertEquals("Errors in the Questions API initialisation: number of questions", 1,
				signedRequest.getJSONArray("questions").length());
		assertEquals("Errors in the Questions API initialisation: consumer key", consumerKey,
				signedRequest.get("consumer_key"));
	}

	public void testInitEventsGenerate()
		throws java.lang.Exception
	{
		System.out.println("Init Events: Generate");

		JSONArray users = new JSONArray();
		users.put("brianmoser");
		users.put("hankschrader");

		request = new JSONObject();
		request.put("users", users);

		init = new Init("events", securityObj, consumerSecret, request);
		signedRequest = new JSONObject(init.generate());
		JSONObject signedUsers = signedRequest.getJSONObject("config")
			.getJSONObject("users");

		assertTrue("Error in the Events API initialisation: config",
				signedRequest.has("config"));
		assertTrue("Error in the Events API initialisation: users",
				signedRequest.getJSONObject("config").has("users"));


		assertTrue("Error in the Events API initialisation, missing user: brianmoser",
				signedUsers.has("brianmoser"));
		assertEquals("Error in the Events API initialisation, invalid signature: brianmoser",
				"7224f1cd26c7eaac4f30c16ccf8e143005734089724affe0dd9cbf008b941e2d",
				signedUsers.getString("brianmoser"));
		assertTrue("Error in the Events API initialisation, missing user: hankschrader",
				signedUsers.has("hankschrader"));
		assertEquals("Error in the Events API initialisation, invalid signature: hankschrader",
				"1e94cba9c43295121a8c93c476601f4f54ce1ee93ddc7f6fb681729c90979b7f",
				signedUsers.getString("hankschrader"));
	}

	private static void assertSecurityPacket (String securityObj)
		throws org.json.JSONException
	{
		JSONObject security = new JSONObject(securityObj);

		assertEquals("Unexpected consumer key",
				consumerKey, security.getString("consumer_key"));
		assertEquals("Unexpected user id",
				"12345678", security.getString("user_id"));
		assertEquals("Unexpected timestamp",
				"20140612-0438", security.getString("timestamp"));
	}

	private static void assertSignature (String signature)
	{
		assertEquals("Unexpected signature",
				expectedSignature, signature);
	}
}
