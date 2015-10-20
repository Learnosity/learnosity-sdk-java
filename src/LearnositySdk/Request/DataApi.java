package learnositysdk.request;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
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
	 * @throws Exception
	 */
	public DataApi(String url, Object securityPacket, String secret) throws Exception
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
	 * @throws Exception
	 */
	public DataApi(String url, Object securityPacket, String secret, String action) throws Exception
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
	    if (requestPacket instanceof JSONObject) {
        	this.requestPacket = new JSONObject(requestPacket.toString());
        	this.requestString = requestPacket.toString();
        } else {
        	if (requestPacket instanceof String) {
        		this.requestPacket = new JSONObject((String)requestPacket);
        		this.requestString = (String)requestPacket;
        	} else if (requestPacket instanceof Map) {
        		this.requestPacket = new JSONObject((Map)requestPacket);
        		this.requestString = this.requestPacket.toString();
        	} else {
        		// Try to make a JSONObject out of a hopefully valid java bean
        		this.requestPacket = new JSONObject(requestPacket);
        		this.requestString = this.requestPacket.toString();
        	}
        }
		this.url = url;
		this.securityPacket = securityPacket;
		this.secret = secret;
		this.action = action;
	}

	/**
	 * Function to modify the internal remote object based on a custom configuration
	 * @return JSONObject containing the information of the request
	 * @throws Exception
	 */
	
	public void setRequestConfig(RequestConfig requestConfig)
	{
		this.remote = new Remote(requestConfig);
	}

	/**
	 * Function to make the post request
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
	 * @return JSONObject containing the information of the request
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
     * 
     */
	public void requestRecursive(RequestCallback callback) throws Exception {
		JSONObject response;
		JSONObject body;
		JSONObject meta;
		JSONArray data;
		boolean makeNextRequest;

		do {
			makeNextRequest = false;
			response = this.requestJSONObject();
			body = new JSONObject(response.getString("body"));
			meta = body.getJSONObject("meta");
			data = body.getJSONArray("data");
			if (meta.getBoolean("status") == true) {
				callback.execute(response);
			}
			if (meta.has("next") && data.length() > 0) {
				this.requestPacket.put("next", meta.get("next"));
				this.requestString = this.requestPacket.toString();
				makeNextRequest = true;
			}
		} while (makeNextRequest);
	}
    
    private JSONObject createResponseObject(Remote remote) throws Exception {
    	JSONObject response = new JSONObject();
    	response.put("body", remote.getBody());
    	response.put("contentType",  remote.getContentType());
    	response.put("statusCode", remote.getStatusCode());
    	response.put("error", remote.getError());
    	response.put("timeTaken", remote.getTimeTaken());
    	return response;
    }
}
