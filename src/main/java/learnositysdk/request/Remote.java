package learnositysdk.request;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.NameValuePair;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.io.StringWriter;
import org.apache.http.message.BasicHeader;


/**
*--------------------------------------------------------------------------
* Learnosity SDK - Remote
*--------------------------------------------------------------------------
*
* Used to execute a request to a public endpoint. Useful as a cross
* domain proxy.
*
*/

public class Remote {

	/**
	 * Map to store the result of the requests
	 */
	private Map<String,String> result = null;

	/**
	 * Helper class to build up a query string
	 */
	private StringBuilder sb;

	/**
	 * Data to be used in a post request
	 */
	private Map<String,Object> postData;

	/**
	 * Http client
	 */
	private CloseableHttpClient httpclient;

	/**
	 * Array to store response header information
	 */
	private Header[] headers;

	/**
	 * Array to store request header information
	 */
	private Header[] requestHeaders;

	/**
	 * Consumer identifier for metadata
	 */
	private String consumer = "";

	/**
	 * Action for metadata
	 */
	private String action = "";

	/**
	 * Constructor
	 */
	public Remote()
	{
		result = new HashMap<String,String>();
		sb = new StringBuilder();
		this.setDefaultClient();
	}


	/**
	 * Alternate Constructor taking in requestConfig
	 */
	public Remote(RequestConfig requestConfig)
	{
		result = new HashMap<String,String>();
		sb = new StringBuilder();
		httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
	}

	/**
	 * Set the consumer identifier for metadata
	 * @param consumer The consumer key/identifier
	 */
	public void setConsumer(String consumer)
	{
		this.consumer = consumer != null ? consumer : "";
	}

	/**
	 * Set the action for metadata
	 * @param action The action being performed
	 */
	public void setAction(String action)
	{
		this.action = action != null ? action : "";
	}

	/**
	 * Make a get request to the specified url
	 *
	 * @param url the url of the request
	 */
	public void get(String url) throws Exception
	{
		this.request(url, false);
	}

	/**
	 * Make a get request to the specified url
	 *
	 * @param  url      Full URL of where to GET the request
	 * @param  data     optional get arguments
	 */
	public void get(String url, Map<String,Object> data) throws Exception
	{
		sb.append(url);
		sb.append("?");
		sb.append(this.makeQueryString(data));


		this.request(sb.toString(), false);
	}

	/**
	 * Make a post request to the specified url.
	 *
	 * @param  url      Full URL of where to POST the request
	 * @param  data     post arguments
	 */
	public void post(String url, Map<String, Object> data) throws Exception
	{
		this.postData = data;
		this.request(url, true);
	}

	/**
	 * Set some default values for timeouts, emulating what is in the php sdk.
	 * Not setting redirects as that's enabled by default, with max redirects
	 * set to 50.
	 * Also not setting ssl verification as that is also enabled by default.
	 */
	private void setDefaultClient()
	{
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setConnectTimeout(40000)
				.setSocketTimeout(40000)
				.setConnectionRequestTimeout(10000)
				.build();
		this.httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
	}

	/**
	 * Makes the actual request
	 *
	 * @param  url      Full URL of the request
	 * @param  post     Flag to indicate if this is a post request
	 * @return void
	 */
	private void request(String url, boolean post) throws Exception
	{
		CloseableHttpResponse resp = null;
	    long startTime = System.currentTimeMillis();
	    HttpUriRequest httpRequest;

		if (post) {
			HttpPost httpPost = new HttpPost(url);
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			for (Map.Entry<String, Object> entry: this.postData.entrySet()) {
				String key = entry.getKey();
				entityBuilder.addTextBody(key, entry.getValue().toString(),
					key.equals("action") ? ContentType.TEXT_PLAIN : ContentType.APPLICATION_JSON);
			}
			httpPost.setEntity(entityBuilder.build());
			httpRequest = httpPost;
		} else {
			httpRequest = new HttpGet(url);
		}

		// Add metadata headers
		this.addMetadataHeaders(httpRequest, url, post ? "POST" : "GET");

		resp = this.httpclient.execute(httpRequest);
		InputStream is = resp.getEntity().getContent();
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer, Charset.defaultCharset());
		this.result.put("body", writer.toString());
		this.result.put("total_time", Long.toString(System.currentTimeMillis() - startTime));
		this.result.put("statusCode", Integer.toString(resp.getStatusLine().getStatusCode()));
		this.headers = resp.getAllHeaders();
		if (resp != null) {
			resp.close();
		}
	}

	/**
	 * Returns the body of the response payload as returned by the
	 * URL endpoint
	 *
	 * @return the body of the response, typically a JSON string
	 */
	public String getBody()
	{
		if (this.result.containsKey("body")) {
			return this.result.get("body");
		}
		return "";
	}

	/**
	 * Returns part of the response headers
	 *
	 * @param  type Which key in the headers packet to return
	 * @return      Header from the response packet
	 */
	public String getHeader(String name)
	{
		if (this.headers == null) {
			return "";
		}
		for (Header header : this.headers) {
			if (header.getName().equals(name)) {
				return header.getValue();
			}
		}
		return "";
	}

	/**
	 * Returns all request headers that were sent
	 *
	 * @return Array of request headers
	 */
	public Header[] getRequestHeaders()
	{
		return this.requestHeaders;
	}

	/**
	 * Returns the content type of the response
	 *
	 * @return The response's content type
	 */
	public String getContentType()
	{
		if (this.headers == null) {
			return "";
		}
		for (Header header : this.headers) {
			if (header.getName().equals("content_type")) {
				return header.getValue();
			}
		}
		return "";
	}

	/**
	 * The HTTP status code of the request response
	 *
	 * @return status code
	 */
	public int getStatusCode()
	{
		if (result.containsKey("statusCode")) {
			return Integer.parseInt(this.result.get("statusCode"));
		}
		return -1;
	}

	/**
	 * Total transaction time in milliseconds for last transfer
	 *
	 * @return time in milliseconds
	 */
	public long getTimeTaken()
	{
		if (this.result.containsKey("total_time")) {
			return Long.parseLong(this.result.get("total_time"));
		}
		return -1;
	}

	/**
	 * Create a query string for a http get request using a map as input
	 * @return query String
	 */
	private String makeQueryString(Map<String,Object> values)
	{
		List<NameValuePair> parameters = makeNameValueList(values);
		return URLEncodedUtils.format(parameters, "UTF-8");
	}

	/**
	 * Create a List containing NameValuePairs out of a map
	 * 	 * @return List<NameValuePair>
	 */
	private List<NameValuePair> makeNameValueList(Map<String,Object> values)
	{
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		for(Map.Entry<String, Object> entry : values.entrySet()) {
			parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
		}
		return parameters;
	}

	/**
	 * Add metadata headers to the HTTP request
	 * Includes consumer, action, and SDK language:version
	 *
	 * @param httpRequest The HTTP request to add headers to
	 * @param url The request URL
	 * @param method The HTTP method (GET, POST, etc.)
	 */
	private void addMetadataHeaders(HttpUriRequest httpRequest, String url, String method)
	{
		// Add consumer header if available
		if (!this.consumer.isEmpty()) {
			httpRequest.addHeader("X-Learnosity-Consumer", this.consumer);
		}

		// Add action header - either from setter or derived from URL/method
		String actionHeader = this.action;
		if (actionHeader.isEmpty()) {
			actionHeader = MetadataProvider.deriveAction(url, method);
		}
		if (!actionHeader.isEmpty()) {
			httpRequest.addHeader("X-Learnosity-Action", actionHeader);
		}

		// Add SDK language and version header
		String sdkVersion = MetadataProvider.getSdkLanguageVersion();
		if (!sdkVersion.isEmpty()) {
			httpRequest.addHeader("X-Learnosity-SDK", sdkVersion);
		}

		// Store request headers for later retrieval
		this.requestHeaders = httpRequest.getAllHeaders();
	}
}
