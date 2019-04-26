package learnositysdk.request;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.client.config.RequestConfig;


/**
 *--------------------------------------------------------------------------
 * Learnosity SDK - DataApi
 *--------------------------------------------------------------------------
 *
 * Used to make requests to the Learnosity Data API - including
 * generating the security packet
 *
 */
public class DataApi
{
	/**
	 * Used to store the post options
	 */
	private Map<String,Object> options;

	/**
	 * The Remote instance used for posting a request
	 */
	private Remote remote;

	/**
	 * The Init instance used to initialise the security array
	 */
	private Init init;

	/**
	 * The url for the request
	 */
	private String url;

	/**
	 * JSONObject for storing security information
	 */
	private JSONObject secJson;

	/**
	 * JSONObject for storing request data
	 */
	private JSONObject requestPacket;

	/**
	 * String for storing json string
	 */
	private String requestString = "";

	/**
	 * Object for storing the security settings
	 */
	private Object securityPacket;

	/**
	 * String to save the action
	 */
	private String action = "";

	/**
	 * String to save the consumer secret
	 */
	private String secret;

	/**
	 * Constructor
	 * @param url
	 * @param securityPacket
	 * @param secret
	 */
	public DataApi(String url, Object securityPacket, String secret)
	{
		this.remote = new Remote();
		this.url = url;
		this.securityPacket = securityPacket;
		this.secret = secret;
	}

	/**
	 * Constructor
	 * @param url
	 * @param securityPacket
	 * @param secret
	 */
	public DataApi(String url, Object securityPacket, String secret, String action)
	{
		this.remote = new Remote();
		this.url = url;
		this.securityPacket = securityPacket;
		this.secret = secret;
		this.action = action;
	}

	/**
	 * Constructor
	 * @param url
	 * @param securityPacket
	 * @param secret
	 * @param requestPacket
	 * @param action
	 * @throws Exception
	 */
	public DataApi(String url, Object securityPacket, String secret , Object requestPacket, String action) throws Exception
	{
		this.remote = new Remote();

		this.setRequestPacket(
			Init.validateRequestPacket(requestPacket)
		);

		this.url = url;
		this.securityPacket = securityPacket;
		this.secret = secret;
		this.action = action;
	}

	/**
	 * Function to modify the internal remote object based on a custom configuration
	 * @return JSONObject containing the information of the request
	 */

	public void setRequestConfig(RequestConfig requestConfig)
	{
		this.remote = new Remote(requestConfig);
	}

	/**
	 * Function to make the post request
	 * @throws Exception
	 */
	public Remote request() throws Exception
	{
		this.options = new HashMap<String,Object>();
		if (this.action.equals("")) {
			this.init = new Init("data", this.securityPacket, this.secret);
			this.secJson = new JSONObject(init.generate());
		}
		if (!this.action.equals("") && this.requestString.equals("")) {
			this.init = new Init("data", securityPacket, this.secret);
			this.secJson = new JSONObject(init.generate());
		}
		if (!this.action.equals("") && !this.requestString.equals("")) {
			this.init = new Init("data", securityPacket, this.secret, this.requestString);
			this.init.setAction(this.action);
			this.secJson = new JSONObject(init.generate());
			this.options.put("action", action);
			this.options.put("request", this.requestString);
		}
		this.options.put("security", this.secJson.toString());
		this.remote.post(this.url, this.options);
		return remote;
	}

	/**
	 * Function to make the post request
	 * @return JSONObject version of the response from Data API
	 * @throws Exception
	 */
	public JSONObject requestJSONObject() throws Exception
	{
		this.request();
		return this.createResponseObject(this.remote);
	}

	/**
	 * Makes a recursive request to the data api, dependent on
	 * whether 'next' and some data is returned in the meta object.
	 * Executes the callback function for every response.
	 * @param callback An implementation of the RequestCallback to be run with
	 *                 each response from Data API.
	 * @throws Exception
	 */
	public void requestRecursive(RequestCallback callback) throws Exception
	{
		JSONObject response;
		JSONObject body;
		JSONObject meta;
		boolean makeNextRequest;

		do {
			makeNextRequest = false;
			response = this.requestJSONObject();
			body = new JSONObject(response.getString("body"));
			meta = body.getJSONObject("meta");

			if (meta.getBoolean("status") == true) {
				callback.execute(response);
			}

			if (this.responseHasNext(body)) {
				this.requestPacket.put("next", meta.get("next"));
				this.updateRequestString();
				makeNextRequest = true;
			}
		} while (makeNextRequest);
	}

	/**
	 * Sets the request packet, updating the stored string version as well
	 * @param requestPacket The request packet to store
	 */
	private void setRequestPacket(JSONObject requestPacket)
	{
		if (Telemetry.isEnabled()) {
			Telemetry.addToRequest(requestPacket);
		}

		this.requestPacket = requestPacket;
		this.updateRequestString();
	}

	/**
	 * Updates the stored request string using the current request packet
	 */
	private void updateRequestString() {
		this.requestString = this.requestPacket.toString();
	}

	private JSONObject createResponseObject(Remote remote) {
		JSONObject response = new JSONObject();
		response.put("body", remote.getBody());
		response.put("contentType",  remote.getContentType());
		response.put("statusCode", remote.getStatusCode());
		response.put("timeTaken", remote.getTimeTaken());
		return response;
	}

	/**
	 * Determines if the response indicates that there is more data to be
	 * fetched to fully satisfy the request.
	 *
	 * @param responseBody The full "body" field of the response
	 * @return whether there is a next response to get
	 * @throws JSONException if the "data" field of the response is neither an
	 * 		object nor an array
	 */
	private boolean responseHasNext(JSONObject responseBody) throws JSONException
	{
		JSONObject meta = responseBody.getJSONObject("meta");
		Object data = responseBody.get("data");
		int dataLength;

		if (data instanceof JSONObject) {
			dataLength = ((JSONObject) data).length();
		} else {
			dataLength = ((JSONArray) data).length();
		}

		return meta.has("next") && dataLength > 0;
	}
}
