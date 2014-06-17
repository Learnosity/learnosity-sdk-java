package learnositysdk.request;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

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
	 * Constructor
	 * @param url
	 * @param securityPacket
	 * @param secret
	 * @throws Exception
	 */
	public DataApi(String url, Object securityPacket, String secret) throws Exception
	{
		this.remote = new Remote();
		this.init = new Init("data", securityPacket, secret);
	
		this.options = new HashMap<String,Object>();
		this.secJson = new JSONObject(init.generate());
		this.secJson.put("consumer_secret", secret);
		this.options.put("security", this.secJson.toString());
		this.url = url;
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
		this.init = new Init("data", securityPacket, secret, action);
	
		this.options = new HashMap<String,Object>();
		this.secJson = new JSONObject(init.generate());
		this.secJson.put("consumer_secret", secret);
		this.options.put("security", this.secJson.toString());
		this.url = url;
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
        } else {
        	if (requestPacket instanceof String) {
        		this.requestPacket = new JSONObject((String)requestPacket);
        	} else if (requestPacket instanceof Map) {
        		this.requestPacket = new JSONObject((Map)requestPacket);      		
        	} else {
        		// Try to make a JSONObject out of a hopefully valid java bean
        		this.requestPacket = new JSONObject(requestPacket);
        	}
        }
		this.init = new Init("data", securityPacket, secret, this.requestPacket, action);
	
		this.options = new HashMap<String,Object>();
		this.secJson = new JSONObject(init.generate());
		this.secJson.put("consumer_secret", secret);
		this.options.put("security", this.secJson.toString());
		this.options.put("action", action);
		this.options.put("request", this.requestPacket.toString());
		this.url = url;
	}
	
	/**
	 * Function to make the post request
	 * @return instance of Remote which can be queried about information of the request
	 * @throws Exception
	 */
	public Remote request() throws Exception
	{
		this.remote.post(this.url, this.options);
		return this.remote;
	}
}
