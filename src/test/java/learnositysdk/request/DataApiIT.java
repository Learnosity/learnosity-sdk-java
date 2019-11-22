package learnositysdk.request;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;
import org.apache.http.client.config.RequestConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataApiIT {

	static private String consumerKey = "yis0TYCu7U9V4o7M";
	static private String consumerSecret = "74c5fd430cf1242a527f6223aebd42d30464be22";

	private String baseUrl;
	static Map<String,String> securityMap;
	private RequestConfig requestConfig;
	private Map request;
	private DataApi dataApi;
	static JSONObject response;
	private JSONObject responseJson;

	@BeforeEach
	public void setUp()
		throws java.lang.Exception
	{
		String testEnv = System.getenv("ENV");
		String testRegion = System.getenv("REGION");
		String testVersion = System.getenv("VER");

		baseUrl = buildBaseUrl(testEnv, testRegion, testVersion);

		securityMap = new HashMap<String, String>();
		securityMap.put("consumer_key", consumerKey);
		securityMap.put("domain","localhost");

		requestConfig = RequestConfig.custom().setSocketTimeout(40000).build();

		request = new HashMap<String,String>();
	}

	@Test
	public void testGetActivitites()
		throws java.lang.Exception
	{
		String endpoint = baseUrl + "/itembank/activities";
		System.out.println("Testing Data API call to " + endpoint + " with SET request");

		JSONArray items = new JSONArray();
		items.put("item_2");
		items.put("item_3");

		JSONObject data = new JSONObject();
		data.put("items", items);

		JSONObject activity = new JSONObject();
		activity.put("status","published");
		activity.put("description","My test description Title √");
		activity.put("data", data);
		activity.put("reference", UUID.randomUUID().toString());

		JSONArray activities = new JSONArray();
		activities.put(activity);

		request.put("activities", activities);

		assertDataApiRequestWorks(endpoint, securityMap, consumerSecret, request, "set");
	}

	@Test
	public void testGetItemsEmptyRemote()
		throws java.lang.Exception
	{
		String endpoint = baseUrl + "/itembank/items";
		System.out.println("Testing Data API call to " + endpoint + " with Remote object");

		dataApi = new DataApi(endpoint, securityMap, consumerSecret);
		response = dataApi.requestJSONObject();

		Remote remote = dataApi.request();
		String body = remote.getBody();
		responseJson = new JSONObject(body);

		assertConsistentResponseRemote(remote, responseJson);
	}

	@Test
	public void testGetItemsEmpty()
		throws java.lang.Exception
	{
		String endpoint = baseUrl + "/itembank/items";
		System.out.println("Testing Data API call to " + endpoint + " with empty implicit GET request");

		assertDataApiRequestWorks(endpoint, securityMap, consumerSecret);
	}

	@Test
	public void testExplicitGetItemsEmpty()
		throws java.lang.Exception
	{
		String endpoint = baseUrl + "/itembank/items";
		System.out.println("Testing Data API call to " + endpoint + " with empty explicit GET request");

		dataApi = new DataApi(endpoint, securityMap, consumerSecret, "get");
		response = dataApi.requestJSONObject();
		responseJson = new JSONObject(response.getString("body"));

		assertConsistentResponse(response, responseJson);
	}

	@Test
	public void testGetItemsLimit()
		throws java.lang.Exception
	{
		String endpoint = baseUrl + "/itembank/items";
		System.out.println("Testing Data API call to " + endpoint + " with limited GET request");

		request.put("limit", "10");

		assertDataApiRequestWorks(endpoint, securityMap, consumerSecret, request, "get");
	}

	@Test
	public void testGetItemsRecursive()
		throws java.lang.Exception
	{
		String endpoint = baseUrl + "/itembank/items";
		System.out.println("Testing Data API call to " + endpoint + " with recursive GET request");

		request.put("item_pool_id", "DoNotChange_ForIntegrationTest");
		request.put("limit", "1");

		dataApi = new DataApi(endpoint, securityMap, consumerSecret, request, "get");
		dataApi.requestRecursive(new DataApiITCallback());

		/* Can't assert much here, expecting no exceptions... */
	}

	@Test
	public void testGetQuestionsRecursive()
		throws java.lang.Exception
	{
		String[] itemRefs = {"item_2", "item_3", "item_4"};
		String endpoint = baseUrl + "/itembank/questions";
		System.out.println("Testing Data API call to " + endpoint + " with recursive GET request");

		request.put("item_references", itemRefs);
		request.put("limit", "1");

		dataApi = new DataApi(endpoint, securityMap, consumerSecret, request, "get");
		dataApi.requestRecursive(new DataApiITCallback());

		/* Can't assert much here, expecting no exceptions... */
	}

	private JSONObject assertDataApiRequestWorks(String endpoint, Map securityMap, String consumerSecret)
		throws java.lang.Exception
	{

		dataApi = new DataApi(endpoint, securityMap, consumerSecret);
		response = dataApi.requestJSONObject();
		responseJson = new JSONObject(response.getString("body"));

		assertConsistentResponse(response, responseJson);

		return responseJson;
	}

	private JSONObject assertDataApiRequestWorks(String endpoint, Map securityMap, String consumerSecret, Map request, String action)
		throws java.lang.Exception
	{
		dataApi = new DataApi(endpoint, securityMap, consumerSecret, request, "get");
		response = dataApi.requestJSONObject();
		responseJson = new JSONObject(response.getString("body"));

		assertConsistentResponse(response, responseJson);

		return responseJson;
	}

	public void assertConsistentResponse(JSONObject response, JSONObject responseJson)
		throws org.json.JSONException
	{
		assertConsistentResponseCodeStatus(
				response.getInt("statusCode"),
				responseJson.getJSONObject("meta").getBoolean("status")
				);
	}

	public void assertConsistentResponseRemote(Remote responseRemote, JSONObject responseJson)
		throws org.json.JSONException
	{
		assertConsistentResponseCodeStatus(
				responseRemote.getStatusCode(),
				responseJson.getJSONObject("meta").getBoolean("status")
				);
	}

	private void assertConsistentResponseCodeStatus(int statusCode, boolean status)
		throws org.json.JSONException
	{
		assertTrue(
				(statusCode == 200 && status) || (statusCode != 200 && !status),
				"Inconsistent HTTP status cand and meta status"
				);
	}

	public String buildBaseUrl(String env, String region, String version)
	{
		String envDomain = "";
		String regionDomain = ".learnosity.com";
		if (env != null && !env.equals("prod")) {
			envDomain = "." + env;
		} else if (region != null) {
				regionDomain = region;
		}

		String versionPath = "v1";
		if (env != null && env.equals("vg")) {
			versionPath = "latest";
		} else if (version != null) {
			versionPath = version;
		}

		return "https://data" + envDomain + regionDomain + "/" + versionPath;
	}
}
